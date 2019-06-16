package publisher;

import util.Subscription_close;
import util.Message;
import util.Topic;
import java.util.ArrayList;
import java.util.List;
import subscriber.Subscriber;
import subscriber.SubscriberImpl;
import javax.websocket.Session;

public class PublisherImpl implements Publisher 
{

  private List<Subscriber> subscriberSet;
  private int numPublishers;
  private Topic topic;

  public PublisherImpl(Topic topic) 
  {
    subscriberSet = new ArrayList<Subscriber>();
    numPublishers = 1;
    this.topic = topic;
  }

  @Override
  public void incPublishers() 
  {
    numPublishers++;      
  }

  @Override
  public int decPublishers() 
  {
    numPublishers--;
    return numPublishers;  
  }

  @Override
  public void attachSubscriber(Subscriber subscriber) 
  {
    if (subscriberSet.contains(subscriber) == false)
        subscriberSet.add(subscriber);      
  }

  @Override
  public boolean detachSubscriber(Subscriber subscriber) 
  {
    return subscriberSet.remove(subscriber);
  }

  @Override
  public void detachAllSubscribers() 
  {
    subscriberSet.removeAll(subscriberSet);
  }

  @Override
  public void publish(Message message) 
  {
    // Call method 'onMessage' for each of the subscribers
    for (Subscriber s : subscriberSet)
    {
        s.onMessage(message);
    }
  }
  
  public Subscriber subscriber(Session session) {
    for (Subscriber subscriber : subscriberSet) {
      SubscriberImpl subscriberImpl = (SubscriberImpl) subscriber;
      if (subscriberImpl.session == session) {
        return subscriber;
      }
    }
    return null;
  }
}
