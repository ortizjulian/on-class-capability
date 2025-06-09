CREATE TABLE IF NOT EXISTS capability (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    technology_quantity INT NOT NULL
);

CREATE TABLE IF NOT EXISTS bootcamp_capability(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    capability_id BIGINT NOT NULL,
    bootcamp_id BIGINT NOT NULL,
    FOREIGN KEY (capability_id) REFERENCES capability(id)
);