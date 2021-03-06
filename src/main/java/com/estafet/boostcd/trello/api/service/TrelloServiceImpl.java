package com.estafet.boostcd.trello.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.estafet.boostcd.trello.api.dao.TrelloDAO;
import com.estafet.boostcd.trello.api.model.Card;
import com.estafet.openshift.boost.messages.features.CommitMessage;
@Service
public class TrelloServiceImpl implements TrelloService {

    private TrelloDAO trelloDAO;

    @Override
    public void getTrelloCardDetails(String url, CommitMessage commitMessage) {
        trelloDAO.getTrelloCardDetails(url,commitMessage);
    }

    @Override
    public Card getTrelloCardDetails(String url_extension) {
    	return trelloDAO.getTrelloCardDetails("https://trello.com/c/" + url_extension + ".json?");
    }
    
    @Autowired
    public void setTrelloDAO(TrelloDAO trelloDAO) {
        this.trelloDAO = trelloDAO;
    }
}
