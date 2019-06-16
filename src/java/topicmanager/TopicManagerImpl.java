package topicmanager;

import util.Subscription_check;
import util.Topic;
import util.Topic_check;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import publisher.Publisher;
import publisher.PublisherImpl;
import subscriber.Subscriber;

public class TopicManagerImpl implements TopicManager 
{

  private Map<Topic, Publisher> topicMap;

  public TopicManagerImpl() 
  {
    topicMap = new HashMap<Topic, Publisher>();
  }

  @Override
  public Publisher addPublisherToTopic(Topic topic) 
  {
    Publisher p;
    Topic_check tc = isTopic(topic);
    
    // Retrieve the publisher instance (if topic is already open)
    if (tc.isOpen)
    {
        p = topicMap.get(topic);
        p.incPublishers();
    }   
    // Create a new publisher instance and put it in the HashMap<Topic,Publisher>
    else
    {
        p = new PublisherImpl(topic);
        topicMap.put(topic, p);
    }
    
    return p;
  }

  @Override
  public void removePublisherFromTopic(Topic topic) 
  {
    Publisher p;
    Topic_check tc = isTopic(topic);
    int n_sub = 0;
    
    if (tc.isOpen)
    {
        p = topicMap.get(topic);
        n_sub = p.decPublishers();
        if (n_sub == 0)
        {
            topicMap.remove(topic);
            p.detachAllSubscribers();
        }
    }
  }

  @Override
  public Topic_check isTopic(Topic topic) 
  {
    Topic_check tc; 

    if (topicMap.containsKey(topic))
        tc = new Topic_check(topic,true);
    else
        tc = new Topic_check(topic,false); 

    return tc;
  }

  @Override
  public List<Topic> topics() 
  {
    List<Topic> tl = new ArrayList<Topic>();
    
    for (Topic t : topicMap.keySet())
    {
        tl.add(t);
    }
    
    return tl; 
  }

  @Override
  public Subscription_check subscribe(Topic topic, Subscriber subscriber) 
  {
    Subscription_check sc;
    Publisher p = topicMap.get(topic);
    
    if (p == null)
    {
        sc = new Subscription_check(topic, Subscription_check.Result.NO_TOPIC);
    }
    else
    {
        p.attachSubscriber(subscriber);
        sc = new Subscription_check(topic, Subscription_check.Result.OKAY);
    }
        
    return sc;
  }

  @Override
  public Subscription_check unsubscribe(Topic topic, Subscriber subscriber) 
  {
    Subscription_check sc;
    Publisher p = topicMap.get(topic);
    
    if (p == null)
    {
        sc = new Subscription_check(topic, Subscription_check.Result.NO_TOPIC);
    }
    else
    {
        if (p.detachSubscriber(subscriber) == true)
            sc = new Subscription_check(topic, Subscription_check.Result.OKAY);
        else
            sc = new Subscription_check(topic, Subscription_check.Result.NO_SUBSCRIPTION);
    }
    
    return sc;
  }
  
  public Publisher publisher(Topic topic)
  {
    return topicMap.get(topic);
  }
  
}
