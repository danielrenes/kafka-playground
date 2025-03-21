BUNDLE_VERSION := 1.0.0

PROTO_DIR          := proto
PROTO_FILES        := $(shell find $(PROTO_DIR) -name "*.proto")
PROTO_JAVA_PACKAGE := com.github.danielrenes.sumo.metrics.generated;

SUMO_METRICS_DIR               := sumo-metrics
SUMO_METRICS_DOCKER_IMAGE_NAME := sumo-metrics
SUMO_METRICS_PROTO_OUT_DIR     := $(SUMO_METRICS_DIR)/src/main/java

KAFKA_SUMO_DIR               := kafka-sumo
KAFKA_SUMO_DOCKER_IMAGE_NAME := kafka-sumo
KAFKA_SUMO_PROTO_OUT_DIR     := $(KAFKA_SUMO_DIR)/kafka_sumo/generated

define python_pb2_fix_import
	sed -i -r 's/^import\s+(\w+_pb2)\s+as\s+(\w+)$$/from . import \1 as \2/g' $1
endef

.PHONY: all
all: proto build

.PHONY: build
build: build-sumo-metrics docker

.PHONY: build-sumo-metrics
build-sumo-metrics:
	cd $(SUMO_METRICS_DIR) && mvn clean install

.PHONY: docker
docker: docker-kafka-sumo docker-sumo-metrics

.PHONY:
docker-kafka-sumo:
	cd $(KAFKA_SUMO_DIR) && docker build -t $(KAFKA_SUMO_DOCKER_IMAGE_NAME):$(BUNDLE_VERSION) .

.PHONY:
docker-sumo-metrics:
	cd $(SUMO_METRICS_DIR) && docker build -t $(SUMO_METRICS_DOCKER_IMAGE_NAME):$(BUNDLE_VERSION) .

.PHONY: proto
proto: proto-kafka-sumo proto-sumo-metrics

.PHONY: proto-kafka-sumo
proto-kafka-sumo:
	rm -rf $(KAFKA_SUMO_PROTO_OUT_DIR)
	mkdir -p $(KAFKA_SUMO_PROTO_OUT_DIR)
	touch $(KAFKA_SUMO_PROTO_OUT_DIR)/__init__.py
	protoc -I=proto \
		--python_out=$(KAFKA_SUMO_PROTO_OUT_DIR) \
		--pyi_out=$(KAFKA_SUMO_PROTO_OUT_DIR) \
		$(PROTO_FILES)
	$(foreach fname,$(notdir $(basename $(PROTO_FILES))), \
		$(call python_pb2_fix_import,$(KAFKA_SUMO_PROTO_OUT_DIR)/$(fname)_pb2.py); \
		$(call python_pb2_fix_import,$(KAFKA_SUMO_PROTO_OUT_DIR)/$(fname)_pb2.pyi); \
	)

.PHONY: proto-sumo-metrics
proto-sumo-metrics:
	rm -rf $(SUMO_METRICS_PROTO_OUT_DIR)/$(subst .,/,$(PROTO_JAVA_PACKAGE))
	protoc -I=proto \
		--java_out=$(SUMO_METRICS_PROTO_OUT_DIR) \
		$(PROTO_FILES)
