from __future__ import annotations

import os
import typing as t
from argparse import ArgumentParser
from collections import ChainMap
from dataclasses import MISSING, asdict, dataclass, field, fields

DEFAULT_KAFKA_BROKERS: t.Final[t.Tuple[str, ...]] = ("localhost:9092",)
DEFAULT_TRACI_HOST: t.Final[str] = "localhost"
DEFAULT_TRACI_PORT: t.Final[int] = 8813


@dataclass(frozen=True)
class Config:
    kafka_brokers: t.Tuple[str, ...] = field(default=DEFAULT_KAFKA_BROKERS)
    traci_host: str = field(default=DEFAULT_TRACI_HOST)
    traci_port: int = field(default=DEFAULT_TRACI_PORT)

    @classmethod
    def load(cls) -> Config:
        def remove_defaults(data: t.Dict[str, t.Any]) -> t.Dict[str, t.Any]:
            for f in fields(Config):
                value = data[f.name]
                if value != f.default:
                    continue
                if f.default_factory != MISSING and value != f.default_factory():
                    continue
                del data[f.name]
            return data

        return Config(
            **ChainMap(
                remove_defaults(asdict(cls.from_env())),
                remove_defaults(asdict(cls.from_args())),
                asdict(Config()),
            )
        )

    @staticmethod
    def from_args() -> Config:
        parser = ArgumentParser()
        parser.add_argument("--kafka-broker", type=str, action="extend", nargs="*")
        parser.add_argument("--traci-host", type=str, default=DEFAULT_TRACI_HOST)
        parser.add_argument("--traci-port", type=int, default=DEFAULT_TRACI_PORT)
        args = parser.parse_args()
        return Config(
            kafka_brokers=tuple(args.kafka_broker) if args.kafka_broker else DEFAULT_KAFKA_BROKERS,
            traci_host=args.traci_host,
            traci_port=args.traci_port,
        )

    @staticmethod
    def from_env() -> Config:
        kafka_brokers = os.getenv("KAFKA_BROKERS")
        traci_host = os.getenv("TRACI_HOST")
        traci_port = os.getenv("TRACI_PORT")
        return Config(
            kafka_brokers=(
                tuple(kafka_broker.strip() for kafka_broker in kafka_brokers.split(","))
                if kafka_brokers
                else DEFAULT_KAFKA_BROKERS
            ),
            traci_host=traci_host or DEFAULT_TRACI_HOST,
            traci_port=int(traci_port) if traci_port else DEFAULT_TRACI_PORT,
        )
