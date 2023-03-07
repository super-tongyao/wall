package cn.ityao.wall;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
class TongyaoApplicationTests {

    @Test
    void contextLoads() {

        System.out.println(UUID.randomUUID().toString());
    }

    public static void main(String[] args) {
        System.out.println(UUID.randomUUID().toString());
    }

}
