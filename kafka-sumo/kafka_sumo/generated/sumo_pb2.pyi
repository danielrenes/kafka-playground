from google.protobuf.internal import containers as _containers
from google.protobuf.internal import enum_type_wrapper as _enum_type_wrapper
from google.protobuf import descriptor as _descriptor
from google.protobuf import message as _message
from typing import ClassVar as _ClassVar, Iterable as _Iterable, Mapping as _Mapping, Optional as _Optional, Union as _Union

DESCRIPTOR: _descriptor.FileDescriptor

class SpeedUnit(int, metaclass=_enum_type_wrapper.EnumTypeWrapper):
    __slots__ = ()
    METERS_PER_SECOND: _ClassVar[SpeedUnit]
    KILOMETERS_PER_HOUR: _ClassVar[SpeedUnit]
    MILES_PER_HOUR: _ClassVar[SpeedUnit]

class AccelerationUnit(int, metaclass=_enum_type_wrapper.EnumTypeWrapper):
    __slots__ = ()
    METERS_PER_SECOND_SQUARED: _ClassVar[AccelerationUnit]
    KILOMETERS_PER_HOUR_SQUARED: _ClassVar[AccelerationUnit]
    MILES_PER_HOUR_SQUARED: _ClassVar[AccelerationUnit]

class TimeUnit(int, metaclass=_enum_type_wrapper.EnumTypeWrapper):
    __slots__ = ()
    MILLISECOND: _ClassVar[TimeUnit]
    SECOND: _ClassVar[TimeUnit]
    MINUTE: _ClassVar[TimeUnit]
    HOUR: _ClassVar[TimeUnit]

class SignalState(int, metaclass=_enum_type_wrapper.EnumTypeWrapper):
    __slots__ = ()
    UNKNOWN: _ClassVar[SignalState]
    OFF: _ClassVar[SignalState]
    RED: _ClassVar[SignalState]
    YELLOW: _ClassVar[SignalState]
    GREEN: _ClassVar[SignalState]
METERS_PER_SECOND: SpeedUnit
KILOMETERS_PER_HOUR: SpeedUnit
MILES_PER_HOUR: SpeedUnit
METERS_PER_SECOND_SQUARED: AccelerationUnit
KILOMETERS_PER_HOUR_SQUARED: AccelerationUnit
MILES_PER_HOUR_SQUARED: AccelerationUnit
MILLISECOND: TimeUnit
SECOND: TimeUnit
MINUTE: TimeUnit
HOUR: TimeUnit
UNKNOWN: SignalState
OFF: SignalState
RED: SignalState
YELLOW: SignalState
GREEN: SignalState

class Coordinate(_message.Message):
    __slots__ = ("latitude", "longitude")
    LATITUDE_FIELD_NUMBER: _ClassVar[int]
    LONGITUDE_FIELD_NUMBER: _ClassVar[int]
    latitude: float
    longitude: float
    def __init__(self, latitude: _Optional[float] = ..., longitude: _Optional[float] = ...) -> None: ...

class Speed(_message.Message):
    __slots__ = ("amount", "unit")
    AMOUNT_FIELD_NUMBER: _ClassVar[int]
    UNIT_FIELD_NUMBER: _ClassVar[int]
    amount: float
    unit: SpeedUnit
    def __init__(self, amount: _Optional[float] = ..., unit: _Optional[_Union[SpeedUnit, str]] = ...) -> None: ...

class Acceleration(_message.Message):
    __slots__ = ("amount", "unit")
    AMOUNT_FIELD_NUMBER: _ClassVar[int]
    UNIT_FIELD_NUMBER: _ClassVar[int]
    amount: float
    unit: AccelerationUnit
    def __init__(self, amount: _Optional[float] = ..., unit: _Optional[_Union[AccelerationUnit, str]] = ...) -> None: ...

class Time(_message.Message):
    __slots__ = ("amount", "unit")
    AMOUNT_FIELD_NUMBER: _ClassVar[int]
    UNIT_FIELD_NUMBER: _ClassVar[int]
    amount: float
    unit: TimeUnit
    def __init__(self, amount: _Optional[float] = ..., unit: _Optional[_Union[TimeUnit, str]] = ...) -> None: ...

class Relation(_message.Message):
    __slots__ = ("roadId", "laneId")
    ROADID_FIELD_NUMBER: _ClassVar[int]
    LANEID_FIELD_NUMBER: _ClassVar[int]
    roadId: str
    laneId: str
    def __init__(self, roadId: _Optional[str] = ..., laneId: _Optional[str] = ...) -> None: ...

class Vehicle(_message.Message):
    __slots__ = ("position", "speed", "acceleration", "relation")
    POSITION_FIELD_NUMBER: _ClassVar[int]
    SPEED_FIELD_NUMBER: _ClassVar[int]
    ACCELERATION_FIELD_NUMBER: _ClassVar[int]
    RELATION_FIELD_NUMBER: _ClassVar[int]
    position: Coordinate
    speed: Speed
    acceleration: Acceleration
    relation: Relation
    def __init__(self, position: _Optional[_Union[Coordinate, _Mapping]] = ..., speed: _Optional[_Union[Speed, _Mapping]] = ..., acceleration: _Optional[_Union[Acceleration, _Mapping]] = ..., relation: _Optional[_Union[Relation, _Mapping]] = ...) -> None: ...

class SignalPhase(_message.Message):
    __slots__ = ("state", "remaining")
    STATE_FIELD_NUMBER: _ClassVar[int]
    REMAINING_FIELD_NUMBER: _ClassVar[int]
    state: SignalState
    remaining: Time
    def __init__(self, state: _Optional[_Union[SignalState, str]] = ..., remaining: _Optional[_Union[Time, _Mapping]] = ...) -> None: ...

class Connection(_message.Message):
    __slots__ = ("ingressLaneId", "egressLaneId")
    INGRESSLANEID_FIELD_NUMBER: _ClassVar[int]
    EGRESSLANEID_FIELD_NUMBER: _ClassVar[int]
    ingressLaneId: str
    egressLaneId: str
    def __init__(self, ingressLaneId: _Optional[str] = ..., egressLaneId: _Optional[str] = ...) -> None: ...

class SignalGroup(_message.Message):
    __slots__ = ("phase", "connection")
    PHASE_FIELD_NUMBER: _ClassVar[int]
    CONNECTION_FIELD_NUMBER: _ClassVar[int]
    phase: SignalPhase
    connection: _containers.RepeatedCompositeFieldContainer[Connection]
    def __init__(self, phase: _Optional[_Union[SignalPhase, _Mapping]] = ..., connection: _Optional[_Iterable[_Union[Connection, _Mapping]]] = ...) -> None: ...

class TrafficLight(_message.Message):
    __slots__ = ("signalGroups",)
    SIGNALGROUPS_FIELD_NUMBER: _ClassVar[int]
    signalGroups: _containers.RepeatedCompositeFieldContainer[SignalGroup]
    def __init__(self, signalGroups: _Optional[_Iterable[_Union[SignalGroup, _Mapping]]] = ...) -> None: ...
