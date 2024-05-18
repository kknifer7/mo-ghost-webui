class Dict {
  map: Map<string, DictItem>;

  constructor(map: Map<string, DictItem>) {
    for (const entry of map) {
      entry[1].value = entry[0];
    }
    this.map = map;
  }

  get(key: string) {
    // eslint-disable-next-line @typescript-eslint/no-non-null-assertion
    return this.map.get(key)!;
  }

  values() {
    return Array.from(this.map.values());
  }

  first() {
    return this.map.entries().next().value;
  }
}

class DictItem {
  label: string; // 用于select
  value?: string; // 用于select。与Dict的key相同，延后在所属Dict中进行初始化
  icon?: string; // 用于select
  classe?: string;
  color?: string;

  constructor(label: string, icon?: string, classe?: string, color?: string) {
    this.label = label;
    this.icon = icon;
    this.classe = classe;
    this.color = color;
  }

  static of(label: string, icon?: string, classe?: string, color?: string) {
    return new DictItem(label, icon, classe, color);
  }
}

export { Dict, DictItem };
