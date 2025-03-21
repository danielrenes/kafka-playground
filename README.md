# kafka-playground

```mermaid
flowchart TB

Kafka@{shape: das}
Sumo@{shape: process, label: "Sumo\n(Traffic Simulator)"}
KafkaSumo@{shape: process}
SumoMetrics@{shape: process}
Client@{shape: process}

KafkaSumo--Poll via TraCI API-->Sumo
KafkaSumo--Entities-->Kafka
Kafka--Entities-->SumoMetrics
Client--Query via HTTP-->SumoMetrics
```

## Usage

```shell
make build
docker compose up -d

# Test some endpoints
curl -X GET http://localhost:5000/metrics/vehicles/speed/avg | jq .
curl -X GET http://localhost:5000/metrics/traffic-lights/lanes/vehicles | jq .
```

