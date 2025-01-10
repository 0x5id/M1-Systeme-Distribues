package sd.akka.actor;


import akka.actor.AbstractActor;
import akka.actor.ActorSelection;
import akka.actor.Props;


public class ChangRoberts extends AbstractActor {
  private int id;
  private ActorSelection acteurSuivant;
  
    private ChangRoberts() {}
  
    @Override
    public Receive createReceive() {
      return receiveBuilder()
      .match(RecupId.class, message -> Recup(message.id))
      .match(Debut.class,message -> Start(message.startId))
      .match(Suivant.class, message -> Suivant(message.acteurSuivant))
      .match(msgElection.class,message -> Election(message.id))
          .build();
        }
       
        private void Recup (final int id) {
          this.id = id;
        }
  
        private void Start(int startId){
          if (startId == this.id) {
          System.out.println("Acteur " + id + " demarre l'election");
          acteurSuivant.tell(new msgElection(id), getSelf());
          }
        }
  
        private void Suivant(ActorSelection acteurSuivant) {
          this.acteurSuivant = acteurSuivant;
      }

      private void Election(int autreId) {
        System.out.println("je suis l'acteur "+id+" et je recois l'id "+ autreId);
        if (autreId > id) {
         acteurSuivant.tell(new msgElection(autreId), getSelf());
        } else if (autreId <id) {
          acteurSuivant.tell(new msgElection(id), getSelf());
        }
        else if (autreId == id) {
            System.out.println("Roi :" + id);
        }
    }



      public static Props props() {
		return Props.create(ChangRoberts.class);
	}

  public interface Message {}	

  
  public static class RecupId implements Message {
		public final int id;

		public RecupId(int id) {
			this.id = id;
		}
}

public static class Debut implements Message {
  public final int startId;

  public Debut(int startId) {
    this.startId = startId;
  }
}

public static class Suivant {
  public final ActorSelection acteurSuivant;

  public Suivant(ActorSelection acteurSuivant) {
      this.acteurSuivant = acteurSuivant;
  }
}

public static class msgElection {
  public final int id;

  public msgElection(int id) {
      this.id = id;
  }
}

}
