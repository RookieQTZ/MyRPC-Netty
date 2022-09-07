package com.zqt.rpc.protocol;

import com.zqt.rpc.config.Config;
import com.zqt.rpc.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author zqtstart
 * @create 2022-06-03 21:39
 */
@Slf4j
@ChannelHandler.Sharable
public class MessageCodecSharable extends MessageToMessageCodec<ByteBuf, Message> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, List<Object> out) throws Exception {
        ByteBuf buf = ctx.alloc().buffer();
        // 魔数 4byte
        buf.writeBytes(new byte[]{12,10,15,14});
        // 版本号 1byte
        buf.writeByte(1);
        // 序列化算法 1byte   jdk 0    json 1
        buf.writeByte(Config.getSerializerAlgorithm().ordinal());
        // 指令类型 1byte
        buf.writeByte(msg.getMessageType());
        // 请求序号 4byte
        buf.writeInt(msg.getSequenceId());
        // 补齐 1byte
        buf.writeByte(0xff);
        // 获取内容的字节数组
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        ObjectOutputStream oos = new ObjectOutputStream(bos);
//        oos.writeObject(msg);
//        byte[] bytes = bos.toByteArray();
        // todo
        byte[] bytes = null;
        try {
            bytes = Config.getSerializerAlgorithm().serialize(msg);
        }catch (Exception e){
            e.printStackTrace();
        }

        // 内容长度 4byte
        buf.writeInt(bytes.length);
        // 写入内容
        buf.writeBytes(bytes);
        out.add(buf);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        int magicNum = msg.readInt();
        byte version = msg.readByte();
        byte serializerAlgorithm = msg.readByte();
//        byte messageType = msg.readByte();
        int messageType = msg.readByte();
        int sequence = msg.readInt();
        msg.readByte();
        int length = msg.readInt();
        byte[] bytes = new byte[length];
        msg.readBytes(bytes);

        // 反序列化
//        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
//        ObjectInputStream ois = new ObjectInputStream(bis);
//        Message message = (Message) ois.readObject();
        Class<?> clazz = Message.getMessageClass(messageType);
//        Object message = Config.getSerializerAlgorithm().deserialize(clazz, bytes);
        Object message = Serializer.Algorithm.values()[serializerAlgorithm].deserialize(clazz, bytes);
        System.out.println("decode");
        log.debug("{}, {}, {}, {}, {}, {}", magicNum, version, serializerAlgorithm, message, sequence, length);
        log.debug("{}", message);
        out.add(message);
    }
}
