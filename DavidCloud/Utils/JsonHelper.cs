using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Runtime.Serialization.Json;
using System.Text;
using System.Threading.Tasks;

namespace DavidCloud.Utils
{
    public static class JsonHelper
    {
        /**
         * Json serializer
         */
        public static string JsonSerializer<T>(T t)
        {
            using (MemoryStream memoryStream = new MemoryStream())
            {
                DataContractJsonSerializer serializer = new DataContractJsonSerializer(typeof(T));
                serializer.WriteObject(memoryStream, t);
                return Encoding.UTF8.GetString(memoryStream.ToArray());
            }
        }

        /**
         * Json deserializer
         */
        public static T JsonDeserialize<T>(string jsonString)
        {
            using (MemoryStream memoryStream = new MemoryStream(Encoding.UTF8.GetBytes(jsonString)))
            {
                DataContractJsonSerializer serializer = new DataContractJsonSerializer(typeof(T));
                return (T)serializer.ReadObject(memoryStream);
            }
        }
    }
}
