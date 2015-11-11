// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

import sign.signlink;

public class Class30_Sub2_Sub4_Sub1 extends Class30_Sub2_Sub4
{

    public void method445(int i, int j, boolean flag, boolean flag1)
    {
        if(anInt1526 != -1 && Class20.aClass20Array351[anInt1526].anInt364 == 1)
            anInt1526 = -1;
        if(!flag)
        {
            int k = i - anIntArray1500[0];
            int l = j - anIntArray1501[0];
            if(k >= -8 && k <= 8 && l >= -8 && l <= 8)
            {
                if(anInt1525 < 9)
                    anInt1525++;
                for(int i1 = anInt1525; i1 > 0; i1--)
                {
                    anIntArray1500[i1] = anIntArray1500[i1 - 1];
                    anIntArray1501[i1] = anIntArray1501[i1 - 1];
                    aBooleanArray1553[i1] = aBooleanArray1553[i1 - 1];
                }

                anIntArray1500[0] = i;
                anIntArray1501[0] = j;
                aBooleanArray1553[0] = false;
                return;
            }
        }
        anInt1525 = 0;
        anInt1542 = 0;
        anInt1503 = 0;
        anIntArray1500[0] = i;
        anIntArray1501[0] = j;
        anInt1550 = anIntArray1500[0] * 128 + anInt1540 * 64;
        if(flag1)
            anInt1536 = 42;
        anInt1551 = anIntArray1501[0] * 128 + anInt1540 * 64;
    }

    public void method446(boolean flag)
    {
        if(!flag)
        {
            for(int i = 1; i > 0; i++);
        }
        anInt1525 = 0;
        anInt1542 = 0;
    }

    public void method447(int i, int j, int k, int l)
    {
        for(int i1 = 0; i1 < 4; i1++)
            if(anIntArray1516[i1] <= l)
            {
                anIntArray1514[i1] = k;
                anIntArray1515[i1] = j;
                anIntArray1516[i1] = l + 70;
                return;
            }

        if(i != anInt1509)
            aBoolean1508 = !aBoolean1508;
    }

    public void method448(boolean flag, byte byte0, int i)
    {
        int j = anIntArray1500[0];
        int k = anIntArray1501[0];
        if(i == 0)
        {
            j--;
            k++;
        }
        if(i == 1)
            k++;
        if(i == 2)
        {
            j++;
            k++;
        }
        if(i == 3)
            j--;
        if(i == 4)
            j++;
        if(i == 5)
        {
            j--;
            k--;
        }
        if(i == 6)
            k--;
        if(i == 7)
        {
            j++;
            k--;
        }
        if(anInt1526 != -1 && Class20.aClass20Array351[anInt1526].anInt364 == 1)
            anInt1526 = -1;
        if(anInt1525 < 9)
            anInt1525++;
        for(int l = anInt1525; l > 0; l--)
        {
            anIntArray1500[l] = anIntArray1500[l - 1];
            anIntArray1501[l] = anIntArray1501[l - 1];
            aBooleanArray1553[l] = aBooleanArray1553[l - 1];
        }

        if(byte0 != 20)
        {
            return;
        } else
        {
            anIntArray1500[0] = j;
            anIntArray1501[0] = k;
            aBooleanArray1553[0] = flag;
            return;
        }
    }

    public boolean method449(boolean flag)
    {
        if(!flag)
        {
            for(int i = 1; i > 0; i++);
        }
        return false;
    }

    public Class30_Sub2_Sub4_Sub1()
    {
        anIntArray1500 = new int[10];
        anIntArray1501 = new int[10];
        anInt1502 = -1;
        anInt1504 = 32;
        anInt1505 = -1;
        anInt1507 = 200;
        aBoolean1508 = false;
        anInt1509 = -35698;
        anInt1511 = -1;
        anInt1512 = -1;
        anIntArray1514 = new int[4];
        anIntArray1515 = new int[4];
        anIntArray1516 = new int[4];
        anInt1517 = -1;
        anInt1520 = -1;
        anInt1526 = -1;
        anInt1532 = -1000;
        anInt1535 = 100;
        anInt1536 = -895;
        anInt1540 = 1;
        aBoolean1541 = false;
        aBooleanArray1553 = new boolean[10];
        anInt1554 = -1;
        anInt1555 = -1;
        anInt1556 = -1;
        anInt1557 = -1;
    }

    public int anIntArray1500[];
    public int anIntArray1501[];
    public int anInt1502;
    public int anInt1503;
    public int anInt1504;
    public int anInt1505;
    public String aString1506;
    public int anInt1507;
    public boolean aBoolean1508;
    public int anInt1509;
    public int anInt1510;
    public int anInt1511;
    public int anInt1512;
    public int anInt1513;
    public int anIntArray1514[];
    public int anIntArray1515[];
    public int anIntArray1516[];
    public int anInt1517;
    public int anInt1518;
    public int anInt1519;
    public int anInt1520;
    public int anInt1521;
    public int anInt1522;
    public int anInt1523;
    public int anInt1524;
    public int anInt1525;
    public int anInt1526;
    public int anInt1527;
    public int anInt1528;
    public int anInt1529;
    public int anInt1530;
    public int anInt1531;
    public int anInt1532;
    public int anInt1533;
    public int anInt1534;
    public int anInt1535;
    public int anInt1536;
    public int anInt1537;
    public int anInt1538;
    public int anInt1539;
    public int anInt1540;
    public boolean aBoolean1541;
    public int anInt1542;
    public int anInt1543;
    public int anInt1544;
    public int anInt1545;
    public int anInt1546;
    public int anInt1547;
    public int anInt1548;
    public int anInt1549;
    public int anInt1550;
    public int anInt1551;
    public int anInt1552;
    public boolean aBooleanArray1553[];
    public int anInt1554;
    public int anInt1555;
    public int anInt1556;
    public int anInt1557;
}
