package com.pupilcc.pushbot.service;

import cn.hutool.core.util.IdUtil;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.MessageEntity;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.response.SendResponse;
import com.pupilcc.pushbot.entity.SendMessageDTO;
import com.pupilcc.pushbot.extension.ApiErrorCode;
import com.pupilcc.pushbot.extension.ApiResult;
import com.pupilcc.pushbot.users.Users;
import com.pupilcc.pushbot.users.UsersRepository;
import com.pupilcc.pushbot.utils.ParameterUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

/**
 * 消息的业务处理
 * @author pupilcc
 */
@Service
public class MessageService {
    private final UsersRepository usersRepository;
    private final TelegramBot telegramBot;

    public MessageService(UsersRepository usersRepository, TelegramBot telegramBot) {
        this.usersRepository = usersRepository;
        this.telegramBot = telegramBot;
    }

    /**
     * 发送消息
     * @param dto 消息内容
     * @param chatToken 用户Token
     * @return 响应信息
     */
    public ApiResult<Object> sendMessage(SendMessageDTO dto, String chatToken) {
        // 查找用户
        Users users = usersRepository.findByChatToken(chatToken);
        // 用户不存在
        if (ObjectUtils.isEmpty(users)) {
            return ApiResult.failed(ApiErrorCode.USER_NOT_EXIST);
        }

        // 参数校验
        ApiResult apiResult = ParameterUtils.checkParams(dto);
        if (!apiResult.ok()) {
            return apiResult;
        }
        boolean isSend;

        //发送推送
        isSend = sendMessage(dto.getText(), dto.getParseMode(), users.getChatId());
        return ApiResult.success(isSend);
    }

    /**
     * 发送消息
     * @param text 消息内容
     * @param parseMode 消息格式
     * @param chatId 用户id
     * @return 响应信息
     */
    private boolean sendMessage(String text, ParseMode parseMode, Long chatId) {
        // 给用户发送信息
        SendMessage sendMessage = new SendMessage(chatId, text);
        if (ObjectUtils.isNotEmpty(parseMode)) {
            sendMessage.parseMode(parseMode);
        }
        SendResponse sendResponse = telegramBot.execute(sendMessage);
        return sendResponse.isOk();
    }
}
