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
        assertEquals("Я умею конвертировать основные для России валюты. Напиши /set_currency",
                UpdateHandler.handleText("/help"));
    }

    @Test
    void testStartCommand() {
        assertEquals("Вас приветствует финансовый бот. Я умею: конвертировать основные для России валюты Напиши /set_currency для использования",
                UpdateHandler.handleText("/start"));
    }

    @Test
    void testSetCurrencyCommand() {
        assertEquals("1",
                UpdateHandler.handleText("/set_currency"));
    }
}
