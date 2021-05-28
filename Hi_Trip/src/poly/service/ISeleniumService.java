package poly.service;

import java.util.ArrayList;
import java.util.Map;

import poly.dto.TicketDTO;

public interface ISeleniumService {

	ArrayList<Map<String, String>> getTicket(TicketDTO pDTO);

}
