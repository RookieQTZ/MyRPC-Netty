package com.zqt.rpc.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zqtstart
 * @create 2022-06-03 21:08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractResponseMessage extends Message{
    private boolean isSuccess;
    private String reason;
}
