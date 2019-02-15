public class Event {
        // an event object contains three parts, the type, the the bus stop number, and the bus number
        public String type;
        public int stop;
        public int bus;

        public Event(String type,int stop,int bus){
            this.type = type;
            this.stop = stop;
            this.bus = bus;
        }
}
