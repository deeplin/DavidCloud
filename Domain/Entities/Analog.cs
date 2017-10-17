using System.ComponentModel.DataAnnotations;

namespace Domain.Entities
{
    public class Analog
    {
        [Key]
        public int AnalogId { get; set; }
        public long Time { get; set; }
        public int S1A { get; set; }
        public int S1B { get; set; }
        public int S2 { get; set; }
        public int S3 { get; set; }
        public int A1 { get; set; }
        public int A2 { get; set; }
        public int A3 { get; set; }
        public int F1 { get; set; }
        public int H1 { get; set; }
        public int O1 { get; set; }
        public int O2 { get; set; }
        public int SP { get; set; }
        public int PR { get; set; }
        public int PI { get; set; }
        public int VU { get; set; }

        public void Copy(Analog analog)
        {
            Time = analog.Time;
            S1A = analog.S1A;
            S1B = analog.S1B;
            S2 = analog.S2;
            S3 = analog.S3;
            A1 = analog.A1;
            A2 = analog.A2;
            A3 = analog.A3;
            F1 = analog.F1;
            H1 = analog.H1;
            O1 = analog.O1;
            O2 = analog.O2;
            SP = analog.SP;
            PR = analog.PR;
            PI = analog.PI;
            VU = analog.VU;
        }
    }
}
