using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Sockets;
using System.Reactive;
using System.Reactive.Linq;
using System.Reactive.Subjects;
using System.Text;
using System.Threading.Tasks;

namespace DavidCloud.Network
{
    static class ClientExtensions
    {
        public static ISubject<KeyValuePair<byte[], IPEndPoint>, UdpReceiveResult> 
            ToClientSubject(this UdpClient udpClient)
        {
            return Subject.Create(udpClient.ToClientObserver(), udpClient.ToClientObservable());
        }

        public static IObservable<UdpReceiveResult> ToClientObservable(this UdpClient udpClient)
        {
            return Observable.Create<UdpReceiveResult>(async (observer, token) =>
            {
                try
                {
                    while (!token.IsCancellationRequested)
                    {
                        observer.OnNext(await udpClient.ReceiveAsync());
                    }
                }
                catch (Exception error)
                {
                    observer.OnError(error);
                }
                finally
                {
                    observer.OnCompleted();
                }
            });
        }

        public static IObserver<KeyValuePair<byte[], IPEndPoint>> 
            ToClientObserver(this UdpClient udpClient)
        {
            return Observer.Create<KeyValuePair<byte[], IPEndPoint>>(async pair =>
            {
                await udpClient.SendAsync(pair.Key, pair.Key.Length, pair.Value);
            });
        }
    }
}
