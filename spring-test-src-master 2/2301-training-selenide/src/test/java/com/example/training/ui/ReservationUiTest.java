package com.example.training.ui;

import java.util.Map;

import com.codeborne.selenide.Configuration;
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

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ReservationUiTest {
    @Value("${local.server.port}")
    private int randomPort;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setup() {
        Configuration.baseUrl = "http://localhost:" + randomPort;
    }

    @Test
    @Sql("ReservationUiTest.sql")
    @Sql(value = "clear.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
    public void test_予約する() {
        open("/training/display-list");
        // 研修一覧画面
        $$("tr span").get(0).should(text("ビジネスマナー研修"));
        $$("tr a").get(0).click();
        // 研修詳細画面
        $$("tr span").get(0).should(text("ビジネスマナー研修"));
        $$("tr span").get(1).should(text("2021/01/01 00:00"));
        $$("tr span").get(2).should(text("2021/01/05 00:00"));
        $$("tr span").get(3).should(text("3"));
        $$("tr span").get(4).should(text("5"));
        $("input[value=受講を予約する]").click();
        //  予約フォーム画面
        $("input[name=name]").setValue("東京太郎");
        $("input[name=phone]").setValue("090-0000-0000");
        $("input[name=emailAddress]").setValue("taro@example.com");
        $("select[name=studentTypeCode]").selectOptionByValue("STUDENT");
        $("input[value=予約内容を確認]").click();
        // 予約確認画面
        $("input[value=予約を確定]").click();
        // 予約完了画面
        String reservationId = $("div span").text();
        Map<String, Object> reservationMap = jdbcTemplate.queryForMap("select * from reservation where id=?", reservationId);
        Assertions.assertThat(reservationMap.get("name")).isEqualTo("東京太郎");
    }
}
