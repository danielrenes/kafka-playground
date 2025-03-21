import typing as t

from google.protobuf.message import Message

K_contra = t.TypeVar("K_contra", contravariant=True)
V_contra = t.TypeVar("V_contra", contravariant=True, bound=Message)


Entity = t.Tuple[K_contra, V_contra]
EntityIterator = t.Iterator[Entity[K_contra, V_contra]]
Fetcher = t.Callable[[], EntityIterator[K_contra, V_contra]]


class Feeder(t.Protocol[K_contra, V_contra]):
    def feed(self, key: K_contra, value: V_contra) -> None: ...


class Feed(t.Generic[K_contra, V_contra]):
    def __init__(
        self,
        fetcher: Fetcher[K_contra, V_contra],
        feeder: Feeder[K_contra, V_contra],
    ) -> None:
        self.fetcher = fetcher
        self.feeder = feeder

    def feed(self) -> None:
        for key, value in self.fetcher():
            self.feeder.feed(key, value)
