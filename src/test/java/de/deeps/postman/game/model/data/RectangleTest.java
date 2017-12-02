package de.deeps.postman.game.model.data;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RectangleTest {

    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private Rectangle rectangle;

    @BeforeEach
    void setUp(){
        setRectangle(new Rectangle(100, 0, 100, 100));
    }

    @Test
    void intersects() {
        assert !getRectangle().intersects(new Rectangle(0, 0, 100, 100));
        assert !getRectangle().intersects(new Rectangle(200, 0, 100, 100));
        assert getRectangle().intersects(getRectangle());
        assert getRectangle().intersects(new Rectangle(1, 0, 100, 100));
        assert getRectangle().intersects(new Rectangle(101, 0, 100, 100));
    }

    @Test
    void contains() {
        assert getRectangle().contains(getRectangle());
        assert getRectangle().contains(new Rectangle(110, 10, 80, 80));
        assert !getRectangle().contains(new Rectangle(0, 0, 200, 200));
        assert !getRectangle().contains(new Rectangle(150, 50, 200, 200));
    }

}