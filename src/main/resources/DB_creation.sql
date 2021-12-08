CREATE SCHEMA planner;

CREATE TABLE planner.event_types
(
    event_type_id   SERIAL      NOT NULL PRIMARY KEY,
    event_type_name VARCHAR(30) NOT NULL
);

CREATE TABLE planner.events
(
    event_id      SERIAL      NOT NULL PRIMARY KEY,
    opponent_name VARCHAR(50) NOT NULL,
    date_time     TIMESTAMP   NOT NULL,
    description   VARCHAR(1000),
    event_type_id INTEGER     NOT NULL REFERENCES planner.event_types (event_type_id),
    present       VARCHAR(255)
);