CREATE SCHEMA IF NOT EXISTS traffic_simulator;

CREATE TABLE IF NOT EXISTS traffic_simulator.networks (
    id          BIGINT          PRIMARY KEY AUTO_INCREMENT,
    name        VARCHAR(255)    NOT NULL,
    location    VARCHAR(255)    NOT NULL,
    status      VARCHAR(50)     NOT NULL
);

ALTER TABLE     traffic_simulator.networks
ADD CONSTRAINT  check_status CHECK (status IN ('ACTIVATED', 'DEACTIVATED'));
