import traci

from .feed import Entity, EntityIterator
from .generated.sumo_pb2 import (  # pylint: disable=no-name-in-module
    Acceleration,
    AccelerationUnit,
    Coordinate,
    Relation,
    Speed,
    SpeedUnit,
    Vehicle,
)


def fetch_vehicles() -> EntityIterator[str, Vehicle]:
    for id in traci.vehicle.getIDList():  # pylint: disable=redefined-builtin
        yield fetch_vehicle(id)


def fetch_vehicle(id: str) -> Entity[str, Vehicle]:  # pylint: disable=redefined-builtin
    x, y = traci.vehicle.getPosition(id)
    lon, lat = traci.simulation.convertGeo(x, y)
    speed = traci.vehicle.getSpeed(id)
    acceleration = traci.vehicle.getAcceleration(id)
    road_id = traci.vehicle.getRoadID(id)
    lane_id = traci.vehicle.getLaneID(id)
    assert isinstance(lat, float)
    assert isinstance(lon, float)
    assert isinstance(speed, float)
    assert isinstance(acceleration, float)
    assert isinstance(road_id, str)
    assert isinstance(lane_id, str)
    vehicle = Vehicle(
        position=Coordinate(latitude=lat, longitude=lon),
        speed=Speed(amount=speed, unit=SpeedUnit.METERS_PER_SECOND),
        acceleration=Acceleration(
            amount=acceleration, unit=AccelerationUnit.METERS_PER_SECOND_SQUARED
        ),
        relation=Relation(roadId=road_id, laneId=lane_id),
    )
    return id, vehicle
