yMax=max(magic04(:,1));
yMin=min(magic04(:,1));
x=linspace(yMin,yMax,1000);%�������С����ֳ�1000���ȷֵ�(999�ȷ�),Ȼ��ֱ�����������ĸ���
yy=hist(magic04(:,1),x);%�����������ĸ���
yy=yy/length(magic04(:,1));%�����������ĸ���
bar(x,yy);%���������ܶȺ���ͼ

