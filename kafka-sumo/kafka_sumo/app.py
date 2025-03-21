from __future__ import annotations

import json
import socket
import time
from dataclasses import asdict

import traci
from confluent_kafka import Producer

from .config import Config
from .feeds import create_feeds


def main() -> None:
    config = Config.load()
    print(json.dumps(asdict(config), indent=4, sort_keys=True))
    kafka_config = {
        "bootstrap.servers": ",".join(config.kafka_brokers),
        "client.id": socket.gethostname(),
        "acks": 1,
        "linger.ms": 25,
    }
    producer = Producer(kafka_config)
    feeds = create_feeds(producer)
    traci.init(port=config.traci_port, host=config.traci_host)
    print("Running...")
    try:
        while True:
            traci.simulationStep()
            for feed in feeds:
                feed.feed()
            time.sleep(0.1)
    except KeyboardInterrupt:
        pass
    print("Stopping...")
    try:
        traci.close()
    except traci.TraCIException:
        pass
    producer.flush()
