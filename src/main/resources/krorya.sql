-- Create the database
CREATE DATABASE krorya_db;

-- Enable uuid-ossp extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE users
(
    user_id          UUID        DEFAULT uuid_generate_v4() NOT NULL PRIMARY KEY,
    username         VARCHAR(50)                            NOT NULL,
    email            VARCHAR(100)                           NOT NULL,
    password         VARCHAR(80)                            NOT NULL,
    profile_image    VARCHAR(255)                           NOT NULL,
    bio              VARCHAR(120),
    is_deactivated   BOOLEAN     DEFAULT FALSE,
    role             VARCHAR(50) DEFAULT 'user'::CHARACTER VARYING
        CONSTRAINT users_role_check CHECK ((role)::TEXT = ANY
                                           ((ARRAY ['admin'::character varying, 'user'::character varying])::TEXT[])),
    followings_count INTEGER     DEFAULT 0,
    follower_count   INTEGER     DEFAULT 0
);

CREATE TABLE otps
(
    otp_id             UUID      DEFAULT uuid_generate_v4() NOT NULL PRIMARY KEY,
    otp_code           VARCHAR(6)                           NOT NULL,
    issued_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP  NOT NULL,
    expired_date       TIMESTAMP                            NOT NULL,
    is_verified        BOOLEAN   DEFAULT FALSE              NOT NULL,
    user_id            UUID                                 NOT NULL REFERENCES users ON UPDATE CASCADE ON DELETE CASCADE,
    is_verified_forget BOOLEAN                              NOT NULL
);

CREATE TABLE notifications
(
    notification_id UUID      DEFAULT uuid_generate_v4() NOT NULL PRIMARY KEY,
    user_id         UUID                                 NOT NULL REFERENCES users ON UPDATE CASCADE ON DELETE CASCADE,
    title           VARCHAR(100)                         NOT NULL,
    description     VARCHAR(255)                         NOT NULL,
    type            VARCHAR(50)                          NOT NULL,
    date            TIMESTAMP DEFAULT CURRENT_TIMESTAMP  NOT NULL
);

CREATE TABLE ingredients
(
    ingredient_id UUID DEFAULT uuid_generate_v4() NOT NULL PRIMARY KEY,
    user_id       UUID REFERENCES users ON UPDATE CASCADE ON DELETE CASCADE,
    icon          VARCHAR                         NOT NULL,
    name          VARCHAR(100)                    NOT NULL,
    type          VARCHAR(100)                    NOT NULL
);

CREATE TABLE cuisines
(
    cuisine_id UUID DEFAULT uuid_generate_v4() NOT NULL PRIMARY KEY,
    name       VARCHAR(100)                    NOT NULL
);

CREATE TABLE recipes
(
    recipe_id      UUID      DEFAULT uuid_generate_v4() NOT NULL PRIMARY KEY,
    cuisine_id     UUID                                 NOT NULL REFERENCES cuisines ON UPDATE CASCADE ON DELETE CASCADE,
    image          VARCHAR(255),
    description    VARCHAR(255),
    cooking_level  VARCHAR(100),
    cooking_time   INTEGER,
    serving_number INTEGER,
    title          VARCHAR(100),
    is_draft       BOOLEAN   DEFAULT FALSE,
    is_public      BOOLEAN   DEFAULT FALSE,
    is_bookmarked  BOOLEAN   DEFAULT FALSE,
    creator        UUID                                 NOT NULL REFERENCES users ON UPDATE CASCADE ON DELETE CASCADE,
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP  NOT NULL
);

CREATE TABLE cooking_steps
(
    cooking_step_id UUID DEFAULT uuid_generate_v4() NOT NULL PRIMARY KEY,
    recipe_id       UUID                            NOT NULL REFERENCES recipes ON UPDATE CASCADE ON DELETE CASCADE,
    step_number     INTEGER                         NOT NULL,
    image           VARCHAR(255),
    description     VARCHAR(255)                    NOT NULL
);

CREATE TABLE tags
(
    tag_id UUID DEFAULT uuid_generate_v4() NOT NULL PRIMARY KEY,
    icons  VARCHAR(100)                    NOT NULL,
    name   VARCHAR(150)                    NOT NULL,
    type   VARCHAR(50)                     NOT NULL
);

CREATE TABLE grocery_lists
(
    grocery_id UUID DEFAULT uuid_generate_v4() NOT NULL PRIMARY KEY,
    user_id    UUID                            NOT NULL REFERENCES users ON UPDATE CASCADE ON DELETE CASCADE,
    title      VARCHAR(100)                    NOT NULL
);

CREATE TABLE addresses
(
    address_id      UUID DEFAULT uuid_generate_v4() NOT NULL PRIMARY KEY,
    buyer_id        UUID                            NOT NULL REFERENCES users ON UPDATE CASCADE ON DELETE CASCADE,
    phone_number    VARCHAR(20)                     NOT NULL,
    buyer_latitude  VARCHAR(100)                    NOT NULL,
    buyer_longitude VARCHAR(100)                    NOT NULL
);

CREATE TABLE orders
(
    order_id     UUID      DEFAULT uuid_generate_v4() NOT NULL PRIMARY KEY,
    buyer_id     UUID                                 NOT NULL REFERENCES users ON UPDATE CASCADE ON DELETE CASCADE,
    address_id   UUID                                 NOT NULL REFERENCES addresses ON UPDATE CASCADE ON DELETE CASCADE,
    description  VARCHAR(255)                         NOT NULL,
    total_amount NUMERIC(10, 2)                       NOT NULL,
    created_at   TIMESTAMP DEFAULT NOW(),
    updated_at   TIMESTAMP DEFAULT NOW()
);

CREATE TABLE payments
(
    transaction_id UUID      DEFAULT uuid_generate_v4() NOT NULL PRIMARY KEY,
    buyer_id       UUID                                 NOT NULL REFERENCES users ON UPDATE CASCADE ON DELETE CASCADE,
    payment_status VARCHAR(50),
    order_id       UUID                                 NOT NULL REFERENCES orders ON UPDATE CASCADE ON DELETE CASCADE,
    mode           VARCHAR(50)                          NOT NULL
        CONSTRAINT payments_mode_check CHECK ((mode)::TEXT = ANY
                                              ((ARRAY ['cash'::character varying, 'qrcode'::character varying])::TEXT[])),
    created_at     TIMESTAMP DEFAULT NOW()              NOT NULL,
    updated_at     TIMESTAMP DEFAULT NOW()              NOT NULL
);

CREATE TABLE foods
(
    food_id       UUID             DEFAULT uuid_generate_v4() NOT NULL PRIMARY KEY,
    seller_id     UUID                                        NOT NULL REFERENCES users ON UPDATE CASCADE ON DELETE CASCADE,
    category_id   UUID                                        NOT NULL,
    food_name     VARCHAR(100)                                NOT NULL,
    description   VARCHAR(255)                                NOT NULL,
    price         NUMERIC(10, 2)                              NOT NULL,
    star_average  DOUBLE PRECISION DEFAULT 0,
    total_rater   INTEGER          DEFAULT 0,
    image         VARCHAR(255)                                NOT NULL,
    created_at    TIMESTAMP        DEFAULT NOW()              NOT NULL,
    updated_at    TIMESTAMP        DEFAULT NOW()              NOT NULL,
    is_bookmarked BOOLEAN          DEFAULT FALSE              NOT NULL
);

CREATE TABLE categories
(
    category_id UUID DEFAULT uuid_generate_v4() NOT NULL PRIMARY KEY,
    name        VARCHAR(30)                     NOT NULL,
    icon        VARCHAR(255)                    NOT NULL
);

CREATE TABLE carts
(
    cart_id    UUID      DEFAULT uuid_generate_v4() NOT NULL PRIMARY KEY,
    order_id   UUID                                 NOT NULL REFERENCES orders ON UPDATE CASCADE ON DELETE CASCADE,
    food_id    UUID                                 NOT NULL REFERENCES foods ON UPDATE CASCADE ON DELETE CASCADE,
    quantity   INTEGER                              NOT NULL,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE tokens
(
    tokenid            UUID                     DEFAULT uuid_generate_v4() NOT NULL PRIMARY KEY,
    userid             UUID                                                NOT NULL REFERENCES users ON DELETE CASCADE,
    accesstoken        VARCHAR(512)                                        NOT NULL,
    refreshtoken       VARCHAR(512)                                        NOT NULL,
    accesstokenexpiry  TIMESTAMP WITH TIME ZONE                            NOT NULL,
    refreshtokenexpiry TIMESTAMP WITH TIME ZONE                            NOT NULL,
    created_at         TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE follows
(
    follow_id    UUID DEFAULT uuid_generate_v4() NOT NULL PRIMARY KEY,
    following_id UUID                            NOT NULL REFERENCES users ON UPDATE CASCADE ON DELETE CASCADE,
    follower_id  UUID                            NOT NULL REFERENCES users ON UPDATE CASCADE ON DELETE CASCADE,
    UNIQUE (follower_id, following_id)
);

CREATE TABLE food_bookmark
(
    user_id UUID NOT NULL REFERENCES users ON UPDATE CASCADE ON DELETE CASCADE,
    food_id UUID NOT NULL REFERENCES foods ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY (user_id, food_id)
);

CREATE TABLE recipe_bookmark
(
    user_id   UUID NOT NULL REFERENCES users ON UPDATE CASCADE ON DELETE CASCADE,
    recipe_id UUID NOT NULL REFERENCES recipes ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY (user_id, recipe_id)
);

CREATE TABLE feedbacks
(
    feedback_id   UUID      DEFAULT uuid_generate_v4() NOT NULL PRIMARY KEY,
    parent_id     UUID REFERENCES feedbacks,
    food_id       UUID                                 NOT NULL REFERENCES foods,
    commentator   UUID                                 NOT NULL REFERENCES users,
    comment       VARCHAR                              NOT NULL,
    feedback_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE reports
(
    report_id   UUID      DEFAULT uuid_generate_v4() NOT NULL PRIMARY KEY,
    recipe_id   UUID                                 NOT NULL REFERENCES recipes ON UPDATE CASCADE ON DELETE CASCADE,
    reporter    UUID                                 NOT NULL REFERENCES users ON UPDATE CASCADE ON DELETE CASCADE,
    user_id     UUID                                 NOT NULL REFERENCES users ON UPDATE CASCADE ON DELETE CASCADE,
    description VARCHAR(255)                         NOT NULL,
    created_at  TIMESTAMP DEFAULT NOW(),
    type        VARCHAR(50)
        CONSTRAINT reports_type_check CHECK ((type)::TEXT = ANY
                                             ((ARRAY ['recipe'::character varying, 'user'::character varying])::TEXT[]))
);

CREATE TABLE rates
(
    rate_id UUID DEFAULT uuid_generate_v4() NOT NULL PRIMARY KEY,
    food_id UUID                            NOT NULL REFERENCES foods ON UPDATE CASCADE ON DELETE CASCADE,
    rater   UUID                            NOT NULL REFERENCES users ON UPDATE CASCADE ON DELETE CASCADE,
    star    INTEGER                         NOT NULL
);

CREATE TABLE recipe_tag
(
    recipe_id UUID NOT NULL REFERENCES recipes ON UPDATE CASCADE ON DELETE CASCADE,
    tag_id    UUID NOT NULL REFERENCES tags ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE recipe_ingredient
(
    recipe_id     UUID NOT NULL REFERENCES recipes ON UPDATE CASCADE ON DELETE CASCADE,
    ingredient_id UUID NOT NULL REFERENCES ingredients ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY (recipe_id, ingredient_id)
);

CREATE TABLE grocery_recipe
(
    grocery_id UUID NOT NULL REFERENCES grocery_lists ON UPDATE CASCADE ON DELETE CASCADE,
    recipe_id  UUID NOT NULL REFERENCES recipes ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY (grocery_id, recipe_id)
);

-- Create indexes
CREATE INDEX idx_cuisine_id ON recipes (cuisine_id);
CREATE INDEX idx_cooking_level ON recipes (cooking_level);
CREATE INDEX idx_title ON recipes (title);
CREATE INDEX idx_is_draft ON recipes (is_draft);

CREATE INDEX idx_food_name ON foods (food_name);
CREATE INDEX idx_seller_id ON foods (seller_id);
CREATE INDEX idx_price ON foods (price);

CREATE INDEX idx_following_id ON follows (following_id);
CREATE INDEX idx_follower_id ON follows (follower_id);

CREATE INDEX idx_parent_id ON feedbacks (parent_id);

-- Trigger function to increment followings_count and follower_count on new follow
CREATE OR REPLACE FUNCTION increment_follow_counts() RETURNS TRIGGER
    LANGUAGE plpgsql AS
$$
BEGIN
    UPDATE users SET followings_count = followings_count + 1 WHERE user_id = NEW.follower_id;
    UPDATE users SET follower_count = follower_count + 1 WHERE user_id = NEW.following_id;
    RETURN NEW;
END;
$$;

-- Trigger to execute the function after an insert into follows
CREATE TRIGGER trigger_increment_follow_counts
    AFTER INSERT
    ON follows
    FOR EACH ROW
EXECUTE PROCEDURE increment_follow_counts();

-- Trigger function to decrement followings_count and follower_count on unfollow
CREATE OR REPLACE FUNCTION decrement_follow_counts() RETURNS TRIGGER
    LANGUAGE plpgsql AS
$$
BEGIN
    UPDATE users SET followings_count = followings_count - 1 WHERE user_id = OLD.follower_id;
    UPDATE users SET follower_count = follower_count - 1 WHERE user_id = OLD.following_id;
    RETURN OLD;
END;
$$;

-- Trigger to execute the function after a delete from follows
CREATE TRIGGER trigger_decrement_follow_counts
    AFTER DELETE
    ON follows
    FOR EACH ROW
EXECUTE PROCEDURE decrement_follow_counts();

-- Function to update food rating statistics
CREATE OR REPLACE FUNCTION update_food_rating_stats() RETURNS TRIGGER
    LANGUAGE plpgsql AS
$$
BEGIN
    -- Increment the total_rater for the related food item
    UPDATE foods
    SET total_rater = total_rater + 1
    WHERE food_id = NEW.food_id;

    -- Update the star_average for the related food item
    UPDATE foods
    SET star_average = (SELECT SUM(star)::FLOAT / COUNT(*)
                        FROM rates
                        WHERE food_id = NEW.food_id)
    WHERE food_id = NEW.food_id;

    RETURN NEW;
END;
$$;

-- Trigger to execute the function after an insert into rates
CREATE TRIGGER trigger_update_food_rating_stats
    AFTER INSERT
    ON rates
    FOR EACH ROW
EXECUTE FUNCTION update_food_rating_stats();

INSERT INTO cuisines (name)
VALUES ('Banteay Meanchey'),
       ('Battambang'),
       ('Kampong Cham'),
       ('Kampong Chhnang'),
       ('Kampong Speu'),
       ('Kampong Thom'),
       ('Kampot'),
       ('Kandal'),
       ('Kep'),
       ('Koh Kong'),
       ('Kratié'),
       ('Mondulkiri'),
       ('Oddar Meanchey'),
       ('Pailin'),
       ('Phnom Penh'),
       ('Preah Sihanouk'),
       ('Preah Vihear'),
       ('Pursat'),
       ('Prey Veng'),
       ('Ratanakiri'),
       ('Siem Reap'),
       ('Stung Treng'),
       ('Svay Rieng'),
       ('Takéo'),
       ('Tbong Khmum');

INSERT INTO ingredients (icon, name, type)
VALUES ('icon1', 'apple', 'fruit'),
       ('icon2', 'banana', 'fruit'),
       ('icon3', 'broccoli', 'vegetable'),
       ('icon4', 'tomato', 'vegetable'),
       ('icon5', 'carrot', 'vegetable'),
       ('icon6', 'grapes', 'fruit'),
       ('icon7', 'watermelon', 'fruit'),
       ('icon8', 'cucumber', 'vegetable'),
       ('icon9', 'mushroom', 'vegetable'),
       ('icon10', 'potato', 'vegetable'),
       ('icon11', 'bread', 'grain'),
       ('icon12', 'egg', 'protein'),
       ('icon13', 'chicken', 'protein'),
       ('icon14', 'beef', 'protein'),
       ('icon15', 'rice', 'grain'),
       ('icon16', 'garlic', 'vegetable'),
       ('icon17', 'onion', 'vegetable'),
       ('icon18', 'orange', 'fruit'),
       ('icon19', 'lemon', 'fruit'),
       ('icon20', 'lettuce', 'vegetable');

alter table recipe_ingredient
    add qty varchar(100) not null default 'string';

--