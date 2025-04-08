//package GDG4th.personaChat.chat.application;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.*;
//
//import GDG4th.personaChat.aiAgent.AiAgent;
//import GDG4th.personaChat.chat.application.dto.MessageInfo;
//import GDG4th.personaChat.chat.domain.Chat;
//import GDG4th.personaChat.chat.domain.ChatCache;
//import GDG4th.personaChat.chat.domain.Message;
//import GDG4th.personaChat.chat.persistent.ChatCacheRepository;
//import GDG4th.personaChat.chat.persistent.ChatRepository;
//import GDG4th.personaChat.global.errorHandling.CustomException;
//
//import jakarta.annotation.PostConstruct;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockedStatic;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//@ExtendWith(MockitoExtension.class)
//class ChatServiceTest {
//    private static final Logger log = LoggerFactory.getLogger(ChatServiceTest.class);
//    @Mock
//    private ChatCacheRepository chatCacheRepository;
//
//    @Mock
//    private ChatRepository chatRepository;
//
//    @Mock
//    private AiAgent aiAgent;
//
//    @InjectMocks
//    private ChatService chatService;
//
//    private static final Long USER_ID = 123L;
//    private static final String MBTI = "INTJ";
//    private static final String CONTENT = "Hello, how are you?";
//    private static final int MAX_CHAT_LENGTH = 100;
//
//    /* ChatService.responseMessage Method Test */
//    @Test
//    @DisplayName("유저의 채팅 기록이 어디에도 없는 경우")
//    void test1() {
//        // given
//        // MethodParameter
//        Long userId = 1L;
//        int startOrder = 0;
//
//        // 영속 저장소에 존재하지 않음
//        given(chatRepository.existsById(userId.toString())).willReturn(false);
//
//        // 캐시에도 존재하지 않음
//        given(chatCacheRepository.existsById(userId)).willReturn(false);
//
//        // when
//        List<MessageInfo> messageInfos = chatService.responseMessage(userId, startOrder);
//
//        // then
//        Assertions.assertThat(messageInfos.size()).isEqualTo(0);
//    }
//
//    @Test
//    @DisplayName("유저의 채팅 기록이 캐시에만 있는 경우")
//    void test2() {
//        // given
//        // MethodParameter
//        Long userId = 1L;
//        int startOrder = 0;
//
//        // 영속 저장소에 존재하지 않음
//        // given(chatRepository.existsById(userId.toString())).willReturn(false);
//
//        // 캐시에 존재
//        given(chatCacheRepository.existsById(userId)).willReturn(true);
//
//        // 캐시된 채팅 로그 마지막 order가 9
//        ChatCache chatCache = new ChatCache(userId, "INFJ", new ArrayList<>());
//        for(int i = 0; i < 10; i++) {
//            Message message = new Message("테스트 데이터 " + i, true, i, LocalDateTime.now());
//            chatCache.addCache(message);
//        }
//        Optional<ChatCache> cache = Optional.of(chatCache);
//
//        given(chatCacheRepository.findById(userId)).willReturn(cache);
//
//        // when
//        List<MessageInfo> messageInfos = chatService.responseMessage(userId, startOrder);
//
//        // then
//        Assertions.assertThat(messageInfos.size()).isEqualTo(10);
//        for (int i = 0; i< 10; i++) {
//            Assertions.assertThat(messageInfos.get(i).content()).isEqualTo("테스트 데이터 " + i);
//            Assertions.assertThat(messageInfos.get(i).order()).isEqualTo(i);
//        }
//    }
//
//    @Test
//    @DisplayName("유저의 채팅 기록이 영속 저장소에만 있는 경우")
//    void test3() {
//        // given
//        // MethodParameter
//        Long userId = 1L;
//        int startOrder = 0;
//
//        // 영속 저장소에 존재
//        given(chatRepository.existsById(userId.toString())).willReturn(true);
//
//        // 캐시에 존재
//        given(chatCacheRepository.existsById(userId)).willReturn(false);
//
//        // 저장된 채팅 로그의 마지막 order가 9
//        Chat chat = new Chat(userId.toString(), "INFJ", new ArrayList<>());
//        for(int i = 0; i < 10; i++) {
//            Message message = new Message("테스트 데이터 " + i, true, i, LocalDateTime.now());
//            chat.getMessages().add(message);
//        }
//        Optional<Chat> Ochat = Optional.of(chat);
//
//        given(chatRepository.findById(userId.toString())).willReturn(Ochat);
//
//        // when
//        List<MessageInfo> messageInfos = chatService.responseMessage(userId, startOrder);
//
//        // then
//        Assertions.assertThat(messageInfos.size()).isEqualTo(10);
//        for (int i = 0; i< 10; i++) {
//            Assertions.assertThat(messageInfos.get(i).content()).isEqualTo("테스트 데이터 " + i);
//            Assertions.assertThat(messageInfos.get(i).order()).isEqualTo(i);
//        }
//    }
//
//    @Test
//    @DisplayName("Start Order가 order보다 큰 경우")
//    void test4() {
//        // given
//        // MethodParameter
//        Long userId = 1L;
//        int startOrder = 10;
//
//        // 영속 저장소에 존재
//        given(chatRepository.existsById(userId.toString())).willReturn(true);
//
//        // 캐시에 존재
//        given(chatCacheRepository.existsById(userId)).willReturn(false);
//
//        // 저장된 채팅 로그의 마지막 order가 9
//        Chat chat = new Chat(userId.toString(), "INFJ", new ArrayList<>());
//        for(int i = 0; i < 10; i++) {
//            Message message = new Message("테스트 데이터", true, i, LocalDateTime.now());
//            chat.getMessages().add(message);
//        }
//        Optional<Chat> Ochat = Optional.of(chat);
//
//        given(chatRepository.findById(userId.toString())).willReturn(Ochat);
//
//        // when
//        List<MessageInfo> messageInfos = chatService.responseMessage(userId, startOrder);
//
//        // then
//        Assertions.assertThat(messageInfos.size()).isEqualTo(0);
//    }
//
//    /* ChatService.receiveMessage Method Test */
//
//    @Test
//    void test5() {
//        // Given
//        given(chatCacheRepository.findById(USER_ID)).willReturn(Optional.empty());
//
//        // When
//        chatService.receiveUserMessage(USER_ID, MBTI, CONTENT);
//
//        // Then
//        verify(chatCacheRepository).save(any(ChatCache.class));  // Save method should be called
//    }
//
//    @Test
//    void test6() {
//        // Given
//        ChatCache chatCache = new ChatCache(USER_ID, MBTI, new ArrayList<>());
//        for (int i = 0; i < MAX_CHAT_LENGTH; i++) {
//            chatCache.addCache(new Message("Message " + i, true, i, LocalDateTime.now()));
//        }
//
//        given(chatCacheRepository.findById(USER_ID)).willReturn(Optional.of(chatCache));
//
//        // When
//        chatService.receiveUserMessage(USER_ID, MBTI, CONTENT);
//
//        // Then
//        verify(chatCacheRepository).save(chatCache);  // Should save the chat cache
//        Assertions.assertThat(chatCache.getMessages().isEmpty()).isFalse();  // Cache should be cleared after saving
//    }
//
//    @Test
//    void test7() {
//        // Given
//        ChatCache chatCache = new ChatCache(USER_ID, MBTI, new ArrayList<>());
//        for (int i = 0; i <= MAX_CHAT_LENGTH; i++) {  // Exceed max length
//            chatCache.addCache(new Message("Message " + i, true, i, LocalDateTime.now()));
//        }
//        given(chatCacheRepository.findById(USER_ID)).willReturn(Optional.of(chatCache));
//
//        // When & Then
//        Assertions.assertThatThrownBy(() -> chatService.receiveUserMessage(USER_ID, MBTI, CONTENT))
//                .isInstanceOf(CustomException.class)
//                .hasMessage("Chat log must be even");
//    }
//}