//import org.junit.jupiter.api.Assertions.*;
//import org.junit.jupiter.api.Test;
//import java.util.ArrayList;
//import java.util.*;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.jupiter.api.Assertions.*;
//
//import java.io.*;
//
//public class MessageTest {
//
//    public void testMessageConstructor() {
//        String expectedReceiver = "reciever";
//        String expectedMsg = "this is a test msg";
//        Message message = new Message(expectedReceiver, expectedMsg);
//        assertEquals(expectedReceiver, message.getReceiver(), "Receiver should match the value.");
//        assertEquals(expectedMsg, message.getContent(), "Content should match the text.");
//        assertNull(message.getPhoto(), "Photo should be null for text-only messages.");
//    }
//
//    public void testPhotoMessageConstructor() {
//        String expectedReceive = "reciever";
//        String expectedPhoto = "this is a test photo";
//        Message message = new Message(expectedReceive, expectedPhoto);
//        assertEquals(expectedReceive, message.getReceiver(), "Receiver should match the value.");
//        assertEquals(expectedPhoto, message.getPhoto(), "Content should match the text.");
//        assertNull(message.getContent(), "Photo messages should have null content");
//    }
//
//    }
//
