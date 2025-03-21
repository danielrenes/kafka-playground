FROM python:3.13.2-slim
COPY kafka_sumo /opt/kafka-sumo/kafka_sumo
COPY pyproject.toml /opt/kafka-sumo/
RUN cd /opt/kafka-sumo && pip install .
ENTRYPOINT ["kafka-sumo"]
