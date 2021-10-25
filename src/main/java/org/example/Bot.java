package org.example;

import org.example.Currency;
import org.example.CurrencyConversion;
import org.example.CurrencyMode;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.Default;
import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.generics.LongPollingBot;
import org.telegram.telegrambots.meta.generics.Webhook;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Bot extends TelegramLongPollingBot{
    private final CurrencyMode currencyMode = CurrencyMode.getInstance();
    private final CurrencyConversion currencyConversion =
            CurrencyConversion.getInstance();

    @Override
    @SneakyThrows
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            handleCallback(update.getCallbackQuery());
        } else if (update.hasMessage()) {
            handleMessage(update.getMessage());
        }
    }
    @SneakyThrows
    private void handleCallback(CallbackQuery callbackQuery) {
        Message message = callbackQuery.getMessage();
        String[] param = callbackQuery.getData().split(":");
        String action = param[0];
        Currency newCurrency = Currency.valueOf(param[1]);
        switch (action) {
            case "ORIGINAL":
                currencyMode.setOriginalCurrency(message.getChatId(), newCurrency);
                break;
            case "TARGET":
                currencyMode.setTargetCurrency(message.getChatId(), newCurrency);
                break;
        }
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        Currency originalCurrency = currencyMode.getOriginalCurrency(message.getChatId());
        Currency targetCurrency = currencyMode.getTargetCurrency(message.getChatId());
        for (Currency currency : Currency.values()) {
            buttons.add(
                    Arrays.asList(
                            InlineKeyboardButton.builder()
                                    .text(getCurrencyButton(originalCurrency, currency))
                                    .callbackData("ORIGINAL:" + currency)
                                    .build(),
                            InlineKeyboardButton.builder()
                                    .text(getCurrencyButton(targetCurrency, currency))
                                    .callbackData("TARGET:" + currency)
                                    .build()));
        }
        execute(
                EditMessageReplyMarkup.builder()
                        .chatId(message.getChatId().toString())
                        .messageId(message.getMessageId())
                        .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                        .build());
    }

    @SneakyThrows
    private  void handleMessage(Message message) {
        if (message.hasText() && message.hasEntities()) {
            Optional<MessageEntity> commandEntity =
                    message.getEntities().stream().filter(e -> "bot_command".equals(e.getType())).findFirst();
            if (commandEntity.isPresent()) {
                String command = message.getText().substring(commandEntity.get().getOffset(), commandEntity.get().getLength());
                switch (command) {
                    case "/help":
                    case "/start":
                        execute(SendMessage.builder().text("Вас приветствует срань, " +
                                        "которая отняла миллион времени и сил у ее разработчиков, " +
                                        "я умею конвертировать основные для Росии валюты, используйте " +
                                        "/set_currency для использования")
                                .chatId(message.getChatId().toString()).build());
                        return;
                    case "/set_currency":
                        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
                        Currency originalCurrency = currencyMode.getOriginalCurrency(message.getChatId());
                        Currency targetCurrency = currencyMode.getTargetCurrency(message.getChatId());
                        for (Currency currency : Currency.values()){
                            buttons.add(Arrays.asList(
                                    InlineKeyboardButton.builder().text(getCurrencyButton(targetCurrency, currency)).callbackData("Original:" + currency).build(),
                                    InlineKeyboardButton.builder().text(getCurrencyButton(targetCurrency, currency)).callbackData("Target" + currency).build()));
                        }
                        execute(SendMessage.builder().text("Choose Original and Target currencies")
                                .chatId(message.getChatId()
                                        .toString())
                                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build()).build());

                }
            }
        }
        if (message.hasText()) {
            String messageText = message.getText();
            Optional<Double> value = parseDouble(messageText);
            Currency originalCurrency = currencyMode.getOriginalCurrency(message.getChatId());
            Currency targetCurrency = currencyMode.getTargetCurrency(message.getChatId());
            double ratio = currencyConversion.getConversionRatio(originalCurrency, targetCurrency);
            if (value.isPresent()) {
                execute(
                        SendMessage.builder()
                                .chatId(message.getChatId().toString())
                                .text(
                                        String.format(
                                                "%4.2f %s is %4.2f %s",
                                                value.get(), originalCurrency, (value.get() * ratio), targetCurrency))
                                .build());
                return;
            }
        }
    }

    private Optional<Double> parseDouble(String messageText) {
        try {
            return Optional.of(Double.parseDouble(messageText));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public void clearWebhook() throws TelegramApiRequestException {
        super.clearWebhook();
    }

    private String getCurrencyButton(Currency saved, Currency current) {
        return saved == current ? current + " ✅" : current.name();
    }

    @Override
    public String getBotUsername() {
        return "@VEJAVA_bot";
    }

    @Override
    public String getBotToken() {
        return "2093357793:AAEwdlZ0pTJ4L5D0fFYYTOefcf8-IZJxXCM";
    }

}