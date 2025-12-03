//package com.example.demo.controller.asset;
//
//import static org.hamcrest.Matchers.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//import java.util.Arrays;
//import java.util.List;
//
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.test.web.servlet.MockMvc;
//
//import com.example.demo.model.Asset;
//import com.example.demo.service.asset.AssetService;
//
//@WebMvcTest(AssetsController.class)
//class AssetsControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private AssetService assetService;
//
//    @Test
//    @DisplayName("GET /api/asset/get returns paginated asset list")
//    void getAssets_shouldReturnAssetsPage() throws Exception {
//        // モックデータ作成
//        Asset asset1 = new Asset(); asset1.setId(1L); asset1.setName("Asset A");
//        Asset asset2 = new Asset(); asset2.setId(2L); asset2.setName("Asset B");
//
//        List<Asset> assets = Arrays.asList(asset1, asset2);
//        Page<Asset> page = new PageImpl<>(assets, PageRequest.of(0, 50), assets.size());
//
//        // モックサービスの動作定義
//        Mockito.when(assetService.getAssets("test", PageRequest.of(0, 50)))
//                .thenReturn(page);
//
//        // テスト実行 & 検証
//        mockMvc.perform(get("/api/asset/get")
//                        .param("keyword", "test")
//                        .param("page", "0")
//                        .param("size", "50"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.content", hasSize(2)))
//                .andExpect(jsonPath("$.content[0].id").value(1))
//                .andExpect(jsonPath("$.content[0].name").value("Asset A"))
//                .andExpect(jsonPath("$.content[1].id").value(2))
//                .andExpect(jsonPath("$.content[1].name").value("Asset B"));
//    }
//}
