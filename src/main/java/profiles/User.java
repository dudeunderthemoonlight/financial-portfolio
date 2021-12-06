package profiles;


public class User {

    public InvestmentPortfolio getUserPortfolio() {
        return portfolio;
    }

    private InvestmentPortfolio portfolio = new InvestmentPortfolio();
    private Long chatID;

    public User(Long chatID) {
        this.chatID = chatID;
    }

    public Long getChatID() {
        return chatID;
    }


}
