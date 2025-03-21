import traci

from .feed import Entity, EntityIterator
from .generated.sumo_pb2 import (  # pylint: disable=no-name-in-module
    Connection,
    SignalGroup,
    SignalPhase,
    SignalState,
    Time,
    TimeUnit,
    TrafficLight,
)


def fetch_traffic_lights() -> EntityIterator[str, TrafficLight]:
    for id in traci.trafficlight.getIDList():  # pylint: disable=redefined-builtin
        yield fetch_traffic_light(id)


def fetch_traffic_light(id: str) -> Entity[str, TrafficLight]:  # pylint: disable=redefined-builtin
    phase_duration = traci.trafficlight.getPhaseDuration(id)
    spent_duration = traci.trafficlight.getSpentDuration(id)
    states = traci.trafficlight.getRedYellowGreenState(id)
    links = traci.trafficlight.getControlledLinks(id)
    assert isinstance(phase_duration, float)
    assert isinstance(spent_duration, float)
    assert isinstance(states, str)
    assert isinstance(links, list) and all(
        isinstance(link, list)
        and all(
            isinstance(connection, tuple)
            and len(connection) == 3
            and all(isinstance(param, str) for param in connection)
            for connection in link
        )
        for link in links
    )
    assert len(states) == len(links)
    remaining = Time(amount=phase_duration - spent_duration, unit=TimeUnit.SECOND)
    traffic_light = TrafficLight(
        signalGroups=tuple(
            SignalGroup(
                phase=SignalPhase(state=parse_signal_state(state), remaining=remaining),
                connection=tuple(
                    Connection(ingressLaneId=ingress, egressLaneId=egress)
                    for ingress, egress, _ in link
                ),
            )
            for state, link in zip(states, links)
        ),
    )
    return id, traffic_light


def parse_signal_state(state: str) -> SignalState:
    match state:
        case "r":
            return SignalState.RED
        case "y" | "Y":
            return SignalState.YELLOW
        case "g" | "G":
            return SignalState.GREEN
        case "o" | "O":
            return SignalState.OFF
        case _:
            return SignalState.UNKNOWN
