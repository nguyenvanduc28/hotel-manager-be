package hotelmanager.demo.exceptions.errorobject;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Setter
@Getter
public class MultiMessagesError extends ErrorObject{
    private List<String> messages;

    public MultiMessagesError(int statusCode, List<String> messages, Date timestamp) {
        super(statusCode, timestamp);
        this.messages = messages;
    }
}
