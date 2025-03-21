import typing as t

from confluent_kafka import Producer
from confluent_kafka.serialization import (
    SerializationContext,
    SerializationError,
    Serializer,
    StringSerializer,
)
from google.protobuf.message import EncodeError, Message

from kafka_sumo.feed import Feeder


class ProtobufSerializer(Serializer):  # type:ignore[misc]
    def __call__(
        self,
        obj: Message,
        _: t.Optional[SerializationContext] = None,
    ) -> bytes:
        try:
            return obj.SerializeToString(deterministic=True)
        except EncodeError as e:
            raise SerializationError from e


class ProtobufFeeder(Feeder[str, Message]):
    def __init__(self, producer: Producer, topic: str) -> None:
        self.producer = producer
        self.topic = topic
        self.key_serializer = StringSerializer()
        self.value_serializer = ProtobufSerializer()

    def feed(self, key: str, value: Message) -> None:
        self.producer.produce(
            self.topic,
            key=self.key_serializer(key),
            value=self.value_serializer(value),
        )
        self.producer.poll(1)
