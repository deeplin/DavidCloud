using DavidCloud.Packets;
using NLog;
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

namespace DavidCloud.Networks
{
    static class ClientExtensions
    {
        private static readonly Logger mLogger = LogManager.GetCurrentClassLogger();

        public static ISubject<Packet, UdpReceiveResult>
            ToClientSubject(this UdpClient udpClient, string type)
        {
            return Subject.Create(udpClient.ToClientObserver(), udpClient.ToClientObservable(type));
        }

        public static IObservable<UdpReceiveResult> ToClientObservable(this UdpClient udpClient, String type)
        {
            return Observable.Create<UdpReceiveResult>(async (observer, token) =>
            {
                try
                {
                    while (!token.IsCancellationRequested)
                    {
                        try
                        {
                            UdpReceiveResult udpReceiveResult = await udpClient.ReceiveAsync();
                            observer.OnNext(udpReceiveResult);
                        }catch(Exception e)
                        {
                            mLogger.Error(e, type);
                        }
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

        public static IObserver<Packet>
            ToClientObserver(this UdpClient udpClient)
        {
            return Observer.Create<Packet>(async response =>
            {
                await udpClient.SendAsync(response.Data, response.Length, response.EndPoint);
            });
        }
    }
}
