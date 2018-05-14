yMax=max(magic04(:,1));
yMin=min(magic04(:,1));
x=linspace(yMin,yMax,1000);%将最大最小区间分成1000个等分点(999等分),然后分别计算各个区间的个数
yy=hist(magic04(:,1),x);%计算各个区间的个数
yy=yy/length(magic04(:,1));%计算各个区间的个数
bar(x,yy);%画出概率密度函数图

