package kr.rtustudio.nicknames;

import kr.astria.testing.BaseRSPluginTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NickNamesLoadTest extends BaseRSPluginTest<NickNames> {

    @Override
    protected NickNames createPluginMock() {
        return loadPlugin(NickNames.class);
    }

    // ==================== 플러그인 초기화 ====================

    @Test
    @DisplayName("서버 초기화(MockBukkit) 시 런타임 예외가 발생하지 않고 로드된다")
    void should_load_without_exceptions() {
        assertNotNull(plugin, "플러그인이 정상적으로 로드되지 않았습니다.");
        assertTrue(plugin.isEnabled(), "플러그인이 비활성화된 상태입니다.");
    }

    // ==================== 명령어 ====================

    @Test
    @DisplayName("명령어(/nickname)가 정상적으로 등록되어 있다")
    void should_register_command() {
        verifyCommand("nickname");
    }

    @Test
    @DisplayName("기본 명령어 실행 시 예외가 발생하지 않는다")
    void should_execute_command_without_exception() {
        var player = safeAddPlayer();
        if (player == null) return;
        assertDoesNotThrow(() -> {
            player.performCommand("nickname");
            server.getScheduler().performTicks(20L);
        });
    }

    // ==================== 매니저 ====================

    @Test
    @DisplayName("NameManager가 정상적으로 초기화된다")
    void should_initialize_name_manager() {
        assertNotNull(plugin.getNameManager(), "NameManager가 초기화되지 않았습니다.");
    }

    @Test
    @DisplayName("PlayerNameManager가 정상적으로 초기화된다")
    void should_initialize_player_name_manager() {
        assertNotNull(plugin.getPlayerNameManager(), "PlayerNameManager가 초기화되지 않았습니다.");
    }

    // ==================== 이벤트 시뮬레이션 ====================

    @Test
    @DisplayName("플레이어 입장 시 닉네임 로딩 이벤트가 예외 없이 동작한다")
    void should_handle_player_join_without_exception() {
        var player = safeAddPlayer();
        if (player == null) return;
        assertDoesNotThrow(() -> server.getScheduler().performTicks(40L));
    }

    @Test
    @DisplayName("플레이어 퇴장 시 닉네임 정리 이벤트가 예외 없이 동작한다")
    void should_handle_player_quit_without_exception() {
        var player = safeAddPlayer();
        if (player == null) return;
        assertDoesNotThrow(() -> {
            player.disconnect();
            server.getScheduler().performTicks(20L);
        });
    }
}
