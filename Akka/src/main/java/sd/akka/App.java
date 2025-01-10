package sd.akka;



import java.util.Arrays;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import sd.akka.actor.ChangRoberts;


public class App {
 /**
 * @param args
 */
public static void main(String[] args) {

 int n = 12;
 int ID[] = new int[n];

ActorSystem actorSystem = ActorSystem.create();
    
// attributions des id et vérification ID unique
    for (int i=0;i<n;i++){
        
        int id = (int)(Math.random() * 100) +1;

            for (int j=0 ; j<i ;j++){
                if (id == ID[j]){
                    id = (int)(Math.random() * 100) +1;
                    j=0;
                }
            }

        ID[i]=id;
        ActorRef acteur = actorSystem.actorOf(ChangRoberts.props(), "candidat"+i);
        acteur.tell(new ChangRoberts.RecupId(id), ActorRef.noSender());
      }

      System.out.println(Arrays.toString(ID));
    
   // attribution des acteurs suivants
         for (int i = 0; i < n ;i++) {

            int m = i+1;
            
            if (m == n) {
             m = 0;
             }   

            ActorSelection premierActeur = actorSystem.actorSelection("/user/candidat"+i);
            ActorSelection acteurSuivant = actorSystem.actorSelection("/user/candidat"+m);
            premierActeur.tell(new ChangRoberts.Suivant(acteurSuivant), ActorRef.noSender());
    
        }

// Debut de l'election
        actorSystem.actorSelection("/user/candidat0").tell(new ChangRoberts.Debut(ID[0]), ActorRef.noSender());

        
        actorSystem.terminate(); 
    }
}

 //https://stackoverflow.com/questions/17456302/how-to-find-specific-actor-in-akka