package com.qtechgames.rollernetwork.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qtechgames.rollernetwork.RollerNetworkApplication;
import com.qtechgames.rollernetwork.dto.RollerDTO;
import com.qtechgames.rollernetwork.dto.RollerTransferDTO;
import com.qtechgames.rollernetwork.exceptionhandler.ResourceAlreadyExistsException;
import com.qtechgames.rollernetwork.exceptionhandler.ResourceNotFoundException;
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
        classes = RollerNetworkApplication.class)
@AutoConfigureMockMvc
class RollerControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void givenRollerId_whenGetRoller_thenStatus200AndRollerIsReturned() throws Exception {
        mvc.perform(get("/rollernetwork/roller/{id}", 101).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(101)))
                .andExpect(jsonPath("$.name", is("PLAYER_A2")))
                .andExpect(jsonPath("$.parentId", is(100)))
                .andExpect(jsonPath("$.referralChain", is("100")))
                .andExpect(jsonPath("$.exit", is(false)));
    }

    @Test
    void givenNonExistentRollerId_whenGetRoller_thenReturnsStatus404() throws Exception {
        mvc.perform(get("/rollernetwork/roller/{id}", 200)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(result -> assertEquals("RollerResponse ID not found: 200",
                        result.getResolvedException().getMessage()));
    }

    @Test
    void givenRollerName_whenGetRoller_thenStatus200AndRollerIsReturned() throws Exception {
        mvc.perform(get("/rollernetwork/roller")
                        .param("name", "Player_B2").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(102)))
                .andExpect(jsonPath("$.name", is("PLAYER_B2")))
                .andExpect(jsonPath("$.parentId", is(100)))
                .andExpect(jsonPath("$.referralChain", is("100")))
                .andExpect(jsonPath("$.exit", is(false)));
    }

    @Test
    void givenNonExistentRollerName_whenGetRoller_thenReturnsStatus404() throws Exception {
        mvc.perform(get("/rollernetwork/roller")
                        .param("name", "Player_Z").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(result -> assertEquals("RollerResponse's name not found: Player_Z",
                        result.getResolvedException().getMessage()));
    }

    @Test
    void givenNoRollerInfo_whenGetRoller_thenReturnStatus400() throws Exception {
        mvc.perform(get("/rollernetwork/roller")).andExpect(status().isBadRequest());
    }

    @Test
    void givenRollerName_whenGetRollerDownline_thenStatus200AndRollerDownlineIsReturned() throws Exception {
        MvcResult result = mvc.perform(get("/rollernetwork/roller/{name}/downline", "casino_2").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        List<String> players = mapper.readValue(json, new TypeReference<>() {
        });

        assertNotNull(players);
        assertEquals(14, players.size());
        assertEquals("CASINO_2", players.get(0));
        assertEquals("PLAYER_A2", players.get(1));
        assertEquals("PLAYER_D2", players.get(2));
        assertEquals("PLAYER_E2", players.get(3));
        assertEquals("PLAYER_F2", players.get(4));
        assertEquals("PLAYER_G2", players.get(5));
        assertEquals("PLAYER_B2", players.get(6));
        assertEquals("PLAYER_C2", players.get(7));
        assertEquals("PLAYER_H2", players.get(8));
        assertEquals("PLAYER_I2", players.get(9));
        assertEquals("PLAYER_M2", players.get(10));
        assertEquals("PLAYER_J2", players.get(11));
        assertEquals("PLAYER_K2", players.get(12));
        assertEquals("PLAYER_N2", players.get(13));
    }

    @Test
    void givenNonExistentRollerName_whenGetRollerDownline_thenReturnsStatus404() throws Exception {
        mvc.perform(get("/rollernetwork/roller/{name}/downline", "player_25").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(result -> assertEquals("RollerResponse's name not found on the network: player_25",
                        result.getResolvedException().getMessage()));
    }

    @Test
    void givenRollerName_whenGetRollerReferral_thenHisReferralNameIsReturned() throws Exception {
        mvc.perform(get("/rollernetwork/roller/{name}/referral", "PLAYER_D2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("PLAYER_A2")));
    }

    @Test
    void givenNonExistentRoller_whenGetRollerReferral_thenReturnsStatus404() throws Exception {
        mvc.perform(get("/rollernetwork/roller/{name}/referral", "player_ZZ").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(result -> assertEquals("RollerResponse's name not found on the network: player_ZZ",
                        result.getResolvedException().getMessage()));
    }

    @Test
    void givenRollerNameWithoutAnyReferral_whenGetRollerReferral_thenReturnsStatus404() throws Exception {
        mvc.perform(get("/rollernetwork/roller/{name}/referral", "casino_2").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(result -> assertEquals("The roller does not have any referral: casino_2",
                        result.getResolvedException().getMessage()));
    }

    @Test
    void givenExistingRoller_whenPostRoller_thenReturnsStatus409() throws Exception {
        RollerDTO newRoller = RollerDTO.builder().name("PLAYER_M2").parentName("Player_B2").build();

        mvc.perform(MockMvcRequestBuilders.post("/rollernetwork/roller")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newRoller)))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceAlreadyExistsException))
                .andExpect(result -> assertEquals("RollerResponse already exists on the network: PLAYER_M2",
                        result.getResolvedException().getMessage()));
    }

    @Test
    void givenNewRollerWithNonExistentReferral_whenPostRoller_thenReturnsStatus404() throws Exception {
        RollerDTO newRoller = RollerDTO.builder().name("NEW_PLAYER").parentName("Player_B3").build();

        mvc.perform(MockMvcRequestBuilders.post("/rollernetwork/roller")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newRoller)))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(result -> assertEquals("Referral roller not found: Player_B3",
                        result.getResolvedException().getMessage()));
    }

    @Test
    void givenNewRoller_whenPostRoller_thenReturnsStatus201() throws Exception {
        RollerDTO newRoller = RollerDTO.builder().name("NEW_PLAYER2").parentName("Player_B2").build();

        mvc.perform(MockMvcRequestBuilders.post("/rollernetwork/roller")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newRoller)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("NEW_PLAYER2")))
                .andExpect(jsonPath("$.parentId", is(102)))
                .andExpect(jsonPath("$.referralChain", is("102")))
                .andExpect(jsonPath("$.exit", is(false)));
    }

    @Test
    void givenNonExistentRollerName_whenPutRollerToExit_thenReturnsStatus404() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put("/rollernetwork/roller/{name}/exit", "PLAYER_A3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(result -> assertEquals("RollerResponse's name not found: PLAYER_A3",
                        result.getResolvedException().getMessage()));
    }

    @Test
    void givenRollerName_whenPutRollerToExit_thenReturnsStatus200AndChildReferralIsChanged() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put("/rollernetwork/roller/{name}/exit", "PLAYER_A2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(101)))
                .andExpect(jsonPath("$.name", is("PLAYER_A2")))
                .andExpect(jsonPath("$.exit", is(true)));

        mvc.perform(get("/rollernetwork/roller")
                        .param("name", "Player_D2").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(104)))
                .andExpect(jsonPath("$.name", is("PLAYER_D2")))
                .andExpect(jsonPath("$.parentId", is(100)))
                .andExpect(jsonPath("$.referralChain", is("101,100")))
                .andExpect(jsonPath("$.exit", is(false)));
    }

    @Test
    void givenNonExistentRollerName_whenPutRollerToTransfer_thenReturnsStatus404() throws Exception {
        RollerTransferDTO rollerUpdated = RollerTransferDTO.builder().name("Player_B2").build();

        mvc.perform(MockMvcRequestBuilders.put("/rollernetwork/roller/{name}/transfer", "PLAYER_D3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(rollerUpdated)))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(result -> assertEquals("RollerResponse's name not found: PLAYER_D3",
                        result.getResolvedException().getMessage()));
    }

    @Test
    void givenNonExistentNewReferral_whenPutRollerToTransfer_thenReturnsStatus404() throws Exception {
        RollerTransferDTO rollerUpdated = RollerTransferDTO.builder().name("Player_B20").build();

        mvc.perform(MockMvcRequestBuilders.put("/rollernetwork/roller/{name}/transfer", "PLAYER_D2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(rollerUpdated)))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(result -> assertEquals("New VIP host not found: Player_B20",
                        result.getResolvedException().getMessage()));
    }

    @Test
    void givenNewReferral_whenPutRollerToTransfer_thenReturnsStatus200AndReferralChainIsUpdated() throws Exception {
        RollerTransferDTO rollerUpdated = RollerTransferDTO.builder().name("Player_B2").build();

        mvc.perform(MockMvcRequestBuilders.put("/rollernetwork/roller/{name}/transfer", "PLAYER_H2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(rollerUpdated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(108)))
                .andExpect(jsonPath("$.name", is("PLAYER_H2")))
                .andExpect(jsonPath("$.parentId", is(102)))
                .andExpect(jsonPath("$.referralChain", is("103,102")))
                .andExpect(jsonPath("$.exit", is(false)));
    }
}