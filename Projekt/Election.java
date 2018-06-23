public interface Election extends MsgHandler {
    void startElection(String s);
    String nadiProizvod();//blocks till the leader is known
}
