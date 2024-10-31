package com.lostark.marketplace.event.listener;

import java.io.IOException;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import com.lostark.marketplace.event.MarketDataSavedEvent;
import com.lostark.marketplace.exception.LostArkMarketplaceException;
import com.lostark.marketplace.exception.model.HttpStatusCode;
import com.lostark.marketplace.persist.entity.MarketEntity;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MarketDataElasticsearchListener {
  
  private final ElasticsearchClient elasticsearchClient;
  
  @EventListener
  public void onMarketDataSavedEvent(MarketDataSavedEvent event) {
    event.getMarketEntities().forEach(this::saveToElasticsearch);
  }
  
  private void saveToElasticsearch(MarketEntity entity) {
    IndexRequest<MarketEntity> request = IndexRequest.of(i -> i
        .index("market") // 인덱스 이름
        .id(entity.getItemId().toString()) // 문서 ID로 사용할 값 설정
        .document(entity)); // 저장할 엔티티 데이터
    
    // ElasticsearchClient를 사용해 데이터 저장 요청을 실행
    try {
      this.elasticsearchClient.index(request);
    } catch (IOException e) {
      throw new LostArkMarketplaceException(HttpStatusCode.SERVICE_UNAVAILABLE);
    }
  }
  
}
