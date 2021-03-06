package org.unichain.common.runtime.utils;

import org.unichain.core.Wallet;

public class MUtil {

  private MUtil() {
  }

  public static byte[] convertToUnichainAddress(byte[] address) {
    if (address.length == 20) {
      byte[] newAddress = new byte[21];
      byte[] temp = new byte[]{Wallet.getAddressPreFixByte()};
      System.arraycopy(temp, 0, newAddress, 0, temp.length);
      System.arraycopy(address, 0, newAddress, temp.length, address.length);
      address = newAddress;
    }
    return address;
  }
}
