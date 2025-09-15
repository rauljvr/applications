package com.tech.highrollernetwork.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tech.highrollernetwork.HighRollerNetworkApplication;
import com.tech.highrollernetwork.dto.PlayerRequest;
import com.tech.highrollernetwork.dto.PlayerTransferRequest;
import com.tech.highrollernetwork.exceptionhandler.ResourceAlreadyExistsException;
import com.tech.highrollernetwork.exceptionhandler.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = HighRollerNetworkApplication.class)
@AutoConfigureMockMvc
@Transactional
class PlayerControllerTest {

    public static final String PLAYER_PATH = "/highrollernetwork/player";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void givenPlayerId_whenGetPlayer_thenStatus200AndPlayerIsReturned() throws Exception {
        mvc.perform(get(PLAYER_PATH + "/{id}", 101).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(101)))
                .andExpect(jsonPath("$.name", is("PLAYER_A")))
                .andExpect(jsonPath("$.parentId", is(100)))
                .andExpect(jsonPath("$.referralChain", is("100")))
                .andExpect(jsonPath("$.exit", is(false)));
    }

    @Test
    void givenNonExistentPlayerId_whenGetPlayer_thenReturnsStatus404() throws Exception {
        mvc.perform(get(PLAYER_PATH + "/{id}", 200).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(result -> assertEquals("Player's ID not found: 200",
                        result.getResolvedException().getMessage()));
    }

    @Test
    void givenPlayerName_whenGetPlayer_thenStatus200AndPlayerIsReturned() throws Exception {
        mvc.perform(get(PLAYER_PATH).param("name", "Player_B").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(102)))
                .andExpect(jsonPath("$.name", is("PLAYER_B")))
                .andExpect(jsonPath("$.parentId", is(100)))
                .andExpect(jsonPath("$.referralChain", is("100")))
                .andExpect(jsonPath("$.exit", is(false)));
    }

    @Test
    void givenNonExistentPlayerName_whenGetPlayer_thenReturnsStatus404() throws Exception {
        mvc.perform(get(PLAYER_PATH).param("name", "Player_Z").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(result -> assertEquals("Player's name not found: Player_Z",
                        result.getResolvedException().getMessage()));
    }

    @Test
    void givenNoPlayerInfo_whenGetPlayer_thenReturnStatus400() throws Exception {
        mvc.perform(get(PLAYER_PATH)).andExpect(status().isBadRequest());
    }

    @Test
    void givenPlayerName_whenGetPlayerDownline_thenStatus200AndPlayerDownlineIsReturned() throws Exception {
        MvcResult result = mvc.perform(get(PLAYER_PATH + "/{name}/downline", "casino_2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        List<String> players = mapper.readValue(json, new TypeReference<>() {});
        System.out.println(players);
        assertNotNull(players);
        assertEquals(14, players.size());
        assertEquals("CASINO_2", players.get(0));
        assertEquals("PLAYER_A", players.get(1));
        assertEquals("PLAYER_D", players.get(2));
        assertEquals("PLAYER_E", players.get(3));
        assertEquals("PLAYER_F", players.get(4));
        assertEquals("PLAYER_G", players.get(5));
        assertEquals("PLAYER_B", players.get(6));
        assertEquals("PLAYER_C", players.get(7));
        assertEquals("PLAYER_H", players.get(8));
        assertEquals("PLAYER_I", players.get(9));
        assertEquals("PLAYER_M", players.get(10));
        assertEquals("PLAYER_J", players.get(11));
        assertEquals("PLAYER_K", players.get(12));
        assertEquals("PLAYER_N", players.get(13));
    }

    @Test
    void givenNonExistentPlayerName_whenGetPlayerDownline_thenReturnsStatus404() throws Exception {
        mvc.perform(get(PLAYER_PATH + "/{name}/downline", "Player_25")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(result -> assertEquals("Player's name not found: Player_25",
                        result.getResolvedException().getMessage()));
    }

    @Test
    void givenPlayerName_whenGetPlayerReferral_thenHisReferralNameIsReturned() throws Exception {
        mvc.perform(get(PLAYER_PATH + "/{name}/referrer", "PLAYER_D"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("PLAYER_A")));
    }

    @Test
    void givenNonExistentPlayer_whenGetPlayerReferral_thenReturnsStatus404() throws Exception {
        mvc.perform(get(PLAYER_PATH + "/{name}/referrer", "Player_ZZ")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(result -> assertEquals("Player's name not found: Player_ZZ",
                        result.getResolvedException().getMessage()));
    }

    @Test
    void givenPlayerNameWithoutAnyReferral_whenGetPlayerReferral_thenReturnsStatus404() throws Exception {
        mvc.perform(get(PLAYER_PATH + "/{name}/referrer", "casino_2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(result -> assertEquals("The player does not have any referral: casino_2",
                        result.getResolvedException().getMessage()));
    }

    @Test
    void givenExistingPlayer_whenPostPlayer_thenReturnsStatus409() throws Exception {
        PlayerRequest newPlayer = PlayerRequest.builder().name("PLAYER_M").parentName("Player_B").build();

        mvc.perform(MockMvcRequestBuilders.post(PLAYER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newPlayer)))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceAlreadyExistsException))
                .andExpect(result -> assertEquals("Player already exists on the network: PLAYER_M",
                        result.getResolvedException().getMessage()));
    }

    @Test
    void givenNewPlayerWithNonExistentReferral_whenPostPlayer_thenReturnsStatus404() throws Exception {
        PlayerRequest newPlayer = PlayerRequest.builder().name("NEW_PLAYER").parentName("Player_B2").build();

        mvc.perform(MockMvcRequestBuilders.post(PLAYER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newPlayer)))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(result -> assertEquals("Referral player not found: Player_B2",
                        result.getResolvedException().getMessage()));
    }

    @Test
    void givenNewPlayer_whenPostPlayer_thenReturnsStatus201() throws Exception {
        PlayerRequest newPlayer = PlayerRequest.builder().name("NEW_PLAYER2").parentName("Player_B").build();

        mvc.perform(MockMvcRequestBuilders.post(PLAYER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newPlayer)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("NEW_PLAYER2")))
                .andExpect(jsonPath("$.parentId", is(102)))
                .andExpect(jsonPath("$.referralChain", is("102")))
                .andExpect(jsonPath("$.exit", is(false)));
    }

    @Test
    void givenNonExistentPlayerName_whenPutPlayerToExit_thenReturnsStatus404() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put(PLAYER_PATH + "/{name}/exit", "PLAYER_A2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(result -> assertEquals("Player's name not found: PLAYER_A2",
                        result.getResolvedException().getMessage()));
    }

    @Test
    void givenPlayerName_whenPutPlayerToExit_thenReturnsStatus200AndChildReferralIsChanged() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put(PLAYER_PATH + "/{name}/exit", "PLAYER_A")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(101)))
                .andExpect(jsonPath("$.name", is("PLAYER_A")))
                .andExpect(jsonPath("$.exit", is(true)));

        mvc.perform(get(PLAYER_PATH)
                        .param("name", "Player_D").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(104)))
                .andExpect(jsonPath("$.name", is("PLAYER_D")))
                .andExpect(jsonPath("$.parentId", is(100)))
                .andExpect(jsonPath("$.referralChain", is("101,100")))
                .andExpect(jsonPath("$.exit", is(false)));
    }

    @Test
    void givenNonExistentPlayerName_whenPutPlayerToTransfer_thenReturnsStatus404() throws Exception {
        PlayerTransferRequest playerUpdated = PlayerTransferRequest.builder().name("Player_B").build();

        mvc.perform(MockMvcRequestBuilders.put(PLAYER_PATH + "/{name}/transfer", "PLAYER_D2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(playerUpdated)))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(result -> assertEquals("Player's name not found: PLAYER_D2",
                        result.getResolvedException().getMessage()));
    }

    @Test
    void givenNonExistentNewReferral_whenPutPlayerToTransfer_thenReturnsStatus404() throws Exception {
        PlayerTransferRequest playerUpdated = PlayerTransferRequest.builder().name("Player_B20").build();

        mvc.perform(MockMvcRequestBuilders.put(PLAYER_PATH + "/{name}/transfer", "PLAYER_D")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(playerUpdated)))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(result -> assertEquals("New referrer not found: Player_B20",
                        result.getResolvedException().getMessage()));
    }

    @Test
    void givenNewReferral_whenPutPlayerToTransfer_thenReturnsStatus200AndReferralChainIsUpdated() throws Exception {
        PlayerTransferRequest playerUpdated = PlayerTransferRequest.builder().name("Player_B").build();

        mvc.perform(MockMvcRequestBuilders.put(PLAYER_PATH + "/{name}/transfer", "PLAYER_H")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(playerUpdated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(108)))
                .andExpect(jsonPath("$.name", is("PLAYER_H")))
                .andExpect(jsonPath("$.parentId", is(102)))
                .andExpect(jsonPath("$.referralChain", is("103,102")))
                .andExpect(jsonPath("$.exit", is(false)));
    }
}