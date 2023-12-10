package bg.deliev.quasarapp.web.controller;

import bg.deliev.quasarapp.model.entity.UserEntity;
import bg.deliev.quasarapp.testUtils.TestDataUtil;
import bg.deliev.quasarapp.testUtils.UserTestDataUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
class AllGamesControllerTestIT {

    private static final String TEST_ADMIN = "admin_role";
    private static final String TEST_USER = "user_role";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestDataUtil testDataUtil;

    @Autowired
    private UserTestDataUtil userTestDataUtil;

    @BeforeEach
    void setUp() {
        testDataUtil.cleanUp();
        userTestDataUtil.cleanUp();
    }

    @AfterEach
    void tearDown() {
        testDataUtil.cleanUp();
        userTestDataUtil.cleanUp();
    }

    @Test
    public void testAddRequiresAuthentication() throws Exception {

        mockMvc.perform(
                MockMvcRequestBuilders.get("/games/add")
        ).andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = TEST_USER, roles = {"USER"})
    public void testAddRequiresAdmin() throws Exception {

        mockMvc.perform(
                MockMvcRequestBuilders.get("/games/add")
        ).andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = TEST_ADMIN, roles = {"USER", "ADMIN"})
    public void testAddRequiresAdmin2() throws Exception {

        mockMvc.perform(
                MockMvcRequestBuilders.get("/games/add")
        ).andExpect(status().is2xxSuccessful());
    }




}