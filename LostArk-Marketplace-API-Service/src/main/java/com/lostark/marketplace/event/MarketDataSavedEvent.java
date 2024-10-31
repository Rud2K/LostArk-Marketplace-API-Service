package com.lostark.marketplace.event;

import java.util.List;
import org.springframework.context.ApplicationEvent;
import com.lostark.marketplace.persist.entity.MarketEntity;
import lombok.Getter;

@SuppressWarnings("serial")
@Getter
public class MarketDataSavedEvent extends ApplicationEvent {
  
  private final List<MarketEntity> marketEntities;
  
  public MarketDataSavedEvent(Object source, List<MarketEntity> marketEntities) {
    super(source);
    this.marketEntities = marketEntities;
  }
  
}
