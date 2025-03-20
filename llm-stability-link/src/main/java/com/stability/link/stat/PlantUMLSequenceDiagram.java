package com.stability.link.stat;

import lombok.SneakyThrows;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.StringUtils;
import org.springframework.ai.image.Image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.function.Consumer;

import javax.imageio.ImageIO;

import com.alibaba.fastjson.JSON;

public class PlantUMLSequenceDiagram {
    @SneakyThrows
    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        List<String> javaList = JSON.parseArray(
                "[\"M:com.stability.link.client.BookingController:getBookings() (M)com.stability.link.services"
                    + ".FlightBookingService:getBookings()\",\"M:com.stability.link.services"
                    + ".FlightBookingService:getBookings() (M)com.stability.link.repo.BookingDataRepo:query()\"]")
            .toJavaList(String.class);

        javaList.forEach(m -> {
            String[] s = m.split(" ");

            Arrays.stream(s).forEach(s1 -> {
                s1 = s1.replace("M:", "").replace("(M)", "");
                String[] arr = s1.split(":");
                String className = arr[0];
                String methodName = arr[1];
                map.put(className, methodName);
            });
        });

        Stack<String> stack = new Stack<>();

        StringBuilder umlStr = new StringBuilder();
        map.forEach((s, s2) -> {

            if (stack.empty()) {
                umlStr.append("User -> " + s + ": " + s2 + "\n");
            } else {
                umlStr.append(stack.peek() + " -> " + s + ": " + s2 + "\n");
            }
            stack.push(s);
            stack.peek();
        });

        String last = "";
        while (!stack.isEmpty()) {

            String a = stack.pop();

            if (StringUtils.isEmpty(last)) {
                last = stack.pop();
            }

            if (last != null) {
                umlStr.append(a + " -> " + last + ": return\n");
            } else {
                umlStr.append(a + " -> " + "User" + ": return\n");
            }
        }

        String plantUMLCode = "@startuml\n" +
            "actor User\n" + umlStr + "@enduml";

        String fileName = "1.png";
        OutputStream fos = new FileOutputStream(fileName);
        SourceStringReader reader = new SourceStringReader(plantUMLCode);
        FileFormatOption format = new FileFormatOption(FileFormat.PNG); // 可以选择 PNG, SVG, 等格式
        String output = reader.generateImage(fos, format);
        fos.close();
    }
}