SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS categories;

CCREATE TABLE categories (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `parent` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS categoriesfeatures;

CREATE TABLE `categoriesfeatures` (
  `catergory_id` int(11) NOT NULL,
  `feature_id` int(11) NOT NULL,
  PRIMARY KEY (`catergory_id`,`feature_id`),
  KEY `feature_id_cf_idx` (`feature_id`),
  CONSTRAINT `category_id_cf` FOREIGN KEY (`catergory_id`) REFERENCES `categories` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `feature_id_cf` FOREIGN KEY (`feature_id`) REFERENCES `features` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS features;

CREATE TABLE `features` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(300) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS goods;

CREATE TABLE `goods` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `shrotname` varchar(45) DEFAULT '',
  `description` varchar(300) DEFAULT '',
  `code` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS goods_categories;

CREATE TABLE `goods_categories` (
  `good_id` int(11) NOT NULL,
  `category_id` int(11) NOT NULL,
  PRIMARY KEY (`good_id`,`category_id`),
  KEY `category_id_gc_idx` (`category_id`),
  CONSTRAINT `category_id_gc` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `good_id_gc` FOREIGN KEY (`good_id`) REFERENCES `goods` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS goodsfeatures;

CREATE TABLE `goodsfeatures` (
  `good_id` int(11) NOT NULL,
  `feature_id` int(11) NOT NULL,
  `value` varchar(300) NOT NULL,
  PRIMARY KEY (`good_id`,`feature_id`),
  KEY `feature_id_idx` (`feature_id`),
  CONSTRAINT `feature_id` FOREIGN KEY (`feature_id`) REFERENCES `features` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `good_id` FOREIGN KEY (`good_id`) REFERENCES `goods` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS orders;

CREATE TABLE `orders` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_number` varchar(45) NOT NULL,
  `order_datye` datetime NOT NULL,
  `price_type` int(11) NOT NULL,
  `reg_client` int(11) DEFAULT NULL,
  `del_address` varchar(200) NOT NULL,
  `total` decimal(10,0) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `reg_client_or_idx` (`reg_client`),
  KEY `price_type_or_idx` (`price_type`),
  CONSTRAINT `price_type_or` FOREIGN KEY (`price_type`) REFERENCES `price_types` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `reg_client_or` FOREIGN KEY (`reg_client`) REFERENCES `registered_clients` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS orders_goods;

CREATE TABLE `orders_goods` (
  `order_id` int(11) NOT NULL,
  `good_id` int(11) NOT NULL,
  PRIMARY KEY (`order_id`,`good_id`),
  KEY `good_id_og_idx` (`good_id`),
  CONSTRAINT `good_id_og` FOREIGN KEY (`good_id`) REFERENCES `goods` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `order_id_og` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS price_list;

CREATE TABLE `price_list` (
  `period` datetime NOT NULL,
  `good_id` int(11) NOT NULL,
  `price` varchar(45) NOT NULL,
  PRIMARY KEY (`period`,`good_id`),
  KEY `good_id_idx` (`good_id`),
  CONSTRAINT `good_id_pricelist` FOREIGN KEY (`good_id`) REFERENCES `goods` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS price_types;

CREATE TABLE `price_types` (
  `id` int(11) NOT NULL,
  `name` varchar(45) NOT NULL,
  `percent` decimal(10,0) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS registered_clients;

CREATE TABLE `registered_clients` (
  `id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `main_del_address` varchar(200) DEFAULT NULL,
  `bonuses` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


SET FOREIGN_KEY_CHECKS = 1;