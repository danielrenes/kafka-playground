import typing as t

from confluent_kafka import Producer

from kafka_sumo.traffic_light import fetch_traffic_lights

from .feed import Feed
from .protobuf import ProtobufFeeder
from .vehicle import fetch_vehicles


def create_feeds(producer: Producer) -> t.Tuple[Feed[t.Any, t.Any], ...]:
    return (
        Feed(fetcher=fetch_vehicles, feeder=ProtobufFeeder(producer, "vehicles")),
        Feed(
            fetcher=fetch_traffic_lights,
            feeder=ProtobufFeeder(producer, "traffic-lights"),
        ),
    )
