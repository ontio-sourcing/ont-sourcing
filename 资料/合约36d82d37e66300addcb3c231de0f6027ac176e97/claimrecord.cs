using Ont.SmartContract.Framework;
using Ont.SmartContract.Framework.Services.Ont;
using Ont.SmartContract.Framework.Services.System;
using System;
using System.ComponentModel;
using System.Numerics;
using System.Text;
using Helper = Ont.SmartContract.Framework.Helper;

namespace DID
{
    public class DID : SmartContract
    {
        public static readonly byte[] ADMIN = "AGjD4Mo25kzcStyh1stp7tXkUuMopD43NT".ToScriptHash();

        public delegate void ErrorDelgate(byte[] id, string error);
        public delegate void PushDelgate(byte[] id, string msg, byte[] args);

        [DisplayName("ErrorMsg")]
        public static event ErrorDelgate ErrorEvent;

        [DisplayName("Push")]
        public static event PushDelgate PushEvent;

        public static Object Main(string operation, params object[] args)
        {
            if (operation == "Commit")
            {
                if (args.Length != 3) return false;
                byte[] claimId = (byte[])args[0];
                byte[] commiterId = (byte[])args[1];
                byte[] ownerId = (byte[])args[2];
                return Commit(claimId, commiterId, ownerId);
            }

            if (operation == "Revoke")
            {
                if (args.Length != 2) return false;
                byte[] claimId = (byte[])args[0];
                byte[] ontId = (byte[])args[1];
                return Revoke(claimId, ontId);
            }

            if (operation == "GetStatus")
            {
                if (args.Length != 1) return false;
                byte[] claimId = (byte[])args[0];
                return GetStatus(claimId);
            }

            if (operation == "Upgrade")
            {
                if (args.Length != 1) return false;
                byte[] code = (byte[])args[0];
                return Upgrade(code);
            }

            return false;
        }

        // claimId 文件哈希
        // commiterId 具体描述
        // ownerId ontid
        public static bool Commit(byte[] claimId, byte[] commiterId, byte[] ownerId)
        {
            byte[] v = Storage.Get(Storage.CurrentContext, claimId);

            if (v != null)
            {
                ErrorEvent(claimId, " existed!");
                return false;
            }

            ClaimTx c = new ClaimTx();
            c.claimId = claimId;
            c.status = 1;
            c.commiterId = commiterId;
            c.ownerId = ownerId;

            byte[] tx = Helper.Serialize(c);

            Storage.Put(Storage.CurrentContext, claimId, tx);
            PushEvent(commiterId, " create new claim: ", claimId);
            return true;
        }


        public static bool Revoke(byte[] claimId, byte[] ontId)
        {
            byte[] v = Storage.Get(Storage.CurrentContext, claimId);

            if (v == null)
            {
                ErrorEvent(claimId, " not existed!");
                return false;
            }

            ClaimTx c = (ClaimTx)Helper.Deserialize(v);

            if (c.status != 1)
            {
                ErrorEvent(claimId, " invalid status.");
                return false;
            }
            if (!EqualBytes(c.commiterId, ontId))
            {
                ErrorEvent(ontId, " invalid.");
                return false;
            }

            c.status = 0;

            byte[] tx = Helper.Serialize(c);
            Storage.Put(Storage.CurrentContext, claimId, tx);

            PushEvent(ontId, " revoke claim: ", claimId);
            return true;
        }

        public static byte[] GetStatus(byte[] claimId)
        {
            byte[] v = Storage.Get(Storage.CurrentContext, claimId);
            PushEvent(claimId, " status: ", v);
            return v;
        }

        public static bool Upgrade(byte[] code)
        {
            if (!Runtime.CheckWitness(ADMIN)) return false;
            Contract.Migrate(code, true, "", "", "", "", "");
            return true;
        }

        private static bool EqualBytes(byte[] src, byte[] dst)
        {
            if (src.Length != dst.Length)
            {
                return false;
            }
            for (int i = 0; i < src.Length; i++)
            {
                if (src[i] != dst[i]) return false;
            }
            return true;
        }

        public class ClaimTx
        {
            public byte[] claimId;
            public byte[] commiterId;
            public byte[] ownerId;
            public byte status;
        }
    }
}