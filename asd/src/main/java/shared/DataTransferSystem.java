package shared;

import model.Message;

import java.util.List;

public interface DataTransferSystem {
    void add(Message message);
    Message getByWeight(int weight);
    Message getLightest();
    Message getHeaviest();
    Message deleteLightest();
    Message deleteHeaviest();
    Boolean contains(Message message);
    List<Message> getOrderedByWeight();
    List<Message> getPostOrder();
    List<Message> getPreOrder();
    List<Message> getInOrder();
    int size();
}
