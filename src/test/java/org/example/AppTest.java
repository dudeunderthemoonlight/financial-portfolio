package org.example;

import handlers.UpdateHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class AppTest {

    @BeforeEach
    void setUp() { // ?
    }


    @Test
    void testHelpCommand() {
        assertEquals("Доступные команды:\n" +
                        "/set_currency - конвертация валюты, выберете оригинальную и целевую валюты " +
                        "и отправьте мне число (сумму оригинальной валюты)\n" +
                        "/edit - редактирование инвестиционного портфеля\n" +
                        "/fortfoliovalue - узнать текущую ценность своего портфеля в USD\n",
                UpdateHandler.handleText("/help"));
    }

    @Test
    void testStartCommand() {
        assertEquals("Вас приветствует финансовый бот.\n" +
                        "Я умею:\n" +
                        "-- конвертировать основные для России валюты: /set_currency\n" +
                        "-- хранить и оценивать ваш инвестиционный портфель:\n" +
                        "   /edit для редактирования\n" +
                        "   /portfoliovalue узнать текущую ценность портфеля в USD",
                UpdateHandler.handleText("/start"));
    }

    @Test
    void testSetCurrencyCommand() {
        assertEquals("1",
                UpdateHandler.handleText("/set_currency"));
    }

    @Test
    void testEditCommand() {
        assertEquals("2",
                UpdateHandler.handleText("/edit"));
    }

    @Test
    void testPortfolioValueCommand() {
        assertEquals("3",
                UpdateHandler.handleText("/portfoliovalue"));
    }
}
