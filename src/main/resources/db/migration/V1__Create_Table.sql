CREATE TABLE IF NOT EXISTS `tb_guest`
(
    `id`                      bigint NOT NULL AUTO_INCREMENT,
    `guest_address_city`      varchar(255) DEFAULT NULL,
    `guest_address_number`    int          DEFAULT NULL,
    `guest_address_street`    varchar(255) DEFAULT NULL,
    `guest_email`             varchar(255) DEFAULT NULL,
    `guest_email_domain_name` varchar(255) DEFAULT NULL,
    `guest_email_username`    varchar(255) DEFAULT NULL,
    `first_name`              varchar(255) DEFAULT NULL,
    `last_name`               varchar(255) DEFAULT NULL,
    `stay`                    varchar(255) DEFAULT NULL,
    `guest_telephone`         varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
);