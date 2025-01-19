package com.example.shopping.ui;

import java.util.Map;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import com.codeborne.selenide.Configuration;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class OrderUiTest {
    @Value("${local.server.port}")
    int randomPort;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        Configuration.baseUrl = "http://localhost:" + randomPort;
    }

    @Test
    @Sql("OrderUiTest.sql")
    @Sql(value = "clear.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
    void test_注文() {
        open("/catalog/display-list");
        // 商品一覧画面
        $("a[href*=productId]").click();
        // 商品詳細画面
        $("input[name=quantity]").setValue("2");
        $("input[value=カートに追加]").click();
        //　ショッピングカート画面
        $("input[value=注文する]").click();
        // 注文フォーム画面
        $("input[name=name]").setValue("東京太郎");
        $("input[name=address]").setValue("東京都");
        $("input[name=phone]").setValue("090-0000-0000");
        $("input[name=emailAddress]").setValue("taro@example.com");
        $("input[name=paymentMethod]").selectRadio("CONVENIENCE_STORE");
        $("input[value=注文内容を確認]").click();
        // 注文確認画面
        $("input[value=注文を確定]").click();
        // 注文完了画面
        String orderId = $("div span").text();
        Map<String, Object> reservationMap = jdbcTemplate.queryForMap("SELECT * FROM t_order WHERE id=?", orderId);
        Assertions.assertThat(reservationMap.get("customer_name")).isEqualTo("東京太郎");
    }

    @Test
    @Sql("OrderUiTest.sql")
    @Sql(value = "clear.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
    void test_注文フォーム入力エラー() {
        open("/catalog/display-list");
        $("a[href*=productId]").click();
        $("input[name=quantity]").setValue("2");
        $("input[value=カートに追加]").click();
        $("input[value=注文する]").click();
        $("input[value=注文内容を確認]").click();
        $("td div").should(exactText("名前は必須です"));
    }
}
